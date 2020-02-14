package com.tokopedia.autocomplete;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.autocomplete.initialstate.newfiles.InitialStateData;

import java.util.ArrayList;
import java.util.List;

public class InitialStateViewModel implements Visitable<HostAutoCompleteTypeFactory> {

    private List<InitialStateData> list = new ArrayList<>();
    private String searchTerm = "";

    public void addList(InitialStateData visitable) {
        this.list.add(visitable);
    }

    public List<InitialStateData> getList() {
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
