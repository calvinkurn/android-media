package com.tokopedia.discovery.autocomplete.viewmodel;


import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class CategorySearch implements Visitable<SearchTypeFactory> {

    private List<BaseItemAutoCompleteSearch> list;

    public CategorySearch() {
        list = new ArrayList<>();
    }

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public List<BaseItemAutoCompleteSearch> getList() {
        return list;
    }

    public void setList(List<BaseItemAutoCompleteSearch> list) {
        this.list = list;
    }
}
