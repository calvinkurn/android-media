package com.tokopedia.autocomplete;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.autocomplete.domain.model.SearchData;

import java.util.ArrayList;
import java.util.List;

public class InitialStateViewModel implements Visitable<HostAutoCompleteTypeFactory> {

    private List<SearchData> list;
    private String searchTerm;

    public InitialStateViewModel() {
        this.list = new ArrayList<>();
    }

    public void addList(SearchData visitable) {
        this.list.add(visitable);
    }

    public List<SearchData> getList() {
        return list;
    }

    @Override
    public int type(HostAutoCompleteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
