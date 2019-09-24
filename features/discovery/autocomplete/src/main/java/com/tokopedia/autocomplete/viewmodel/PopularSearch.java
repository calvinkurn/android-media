package com.tokopedia.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.autocomplete.adapter.SearchTypeFactory;

import java.util.List;

public class PopularSearch implements Visitable<SearchTypeFactory> {

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
