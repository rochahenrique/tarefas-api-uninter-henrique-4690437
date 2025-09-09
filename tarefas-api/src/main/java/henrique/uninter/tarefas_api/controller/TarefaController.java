package henrique.uninter.tarefas_api.controller;


import henrique.uninter.tarefas_api.repository.TarefaRepository; // ✅ corrigido

import henrique.uninter.tarefas_api.model.Tarefa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaRepository repository;


    public TarefaController(TarefaRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public List<Tarefa> listar() {
        return repository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody Tarefa tarefa) {
        Tarefa salva = repository.save(tarefa);
        return ResponseEntity.status(201).body(salva); // 201 Created
    }


    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @RequestBody Tarefa dados) {
        return repository.findById(id)
                .map(t -> {
                    t.setNome(dados.getNome());
                    t.setDataEntrega(dados.getDataEntrega());
                    t.setResponsavel(dados.getResponsavel());
                    return ResponseEntity.ok(repository.save(t));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}

