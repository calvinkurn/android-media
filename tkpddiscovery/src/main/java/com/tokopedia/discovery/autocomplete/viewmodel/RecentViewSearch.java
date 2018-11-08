package com.tokopedia.discovery.autocomplete.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

import java.util.List;

public class RecentViewSearch implements Visitable<SearchTypeFactory> {

    private List<BaseItemAutoCompleteSearch> list;

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
