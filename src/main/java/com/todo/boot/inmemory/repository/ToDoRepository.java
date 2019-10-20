package com.todo.boot.inmemory.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.todo.boot.inmemory.model.ToDo;

@Repository
public class ToDoRepository implements CommonRepository<ToDo> {
	private Map<String, ToDo> toDos = new HashMap<>();

	@Override
	public ToDo save(ToDo domain) {
		ToDo result = toDos.get(domain.getId());
		if (result != null) {
			result.setModified(LocalDateTime.now());
			result.setDescription(domain.getDescription());
			result.setCompleted(domain.isCompleted());
			domain = result;
		}
		toDos.put(domain.getId(), domain);
		return toDos.get(domain.getId());
	}

	@Override
	public Iterable<ToDo> save(Collection<ToDo> domains) {
		domains.forEach(this::save);
		return findAll();
	}

	@Override
	public void delete(ToDo domain) {
		toDos.remove(domain.getId());
	}

	@Override
	public ToDo findById(String id) {
		return toDos.get(id);
	}

	@Override
	public Iterable<ToDo> findAll() {
		return toDos.entrySet().stream().sorted(entryComparator).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	private Comparator<Map.Entry<String, ToDo>> entryComparator = (Map.Entry<String, ToDo> o1,Map.Entry<String, ToDo> o2) -> {
		return o1.getValue().getCreated().compareTo(o2.getValue().getCreated());
	};
}
