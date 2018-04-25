package com.tokopedia.discovery.autocomplete;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.domain.model.SearchData;

import java.util.ArrayList;
import java.util.List;

public class DefaultAutoCompleteViewModel implements Visitable<HostAutoCompleteTypeFactory> {

    private List<SearchData> list;

    public DefaultAutoCompleteViewModel() {
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

}
