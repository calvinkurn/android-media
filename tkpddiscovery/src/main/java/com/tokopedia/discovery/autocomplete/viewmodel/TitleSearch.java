package com.tokopedia.discovery.autocomplete.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

public class TitleSearch implements Visitable<SearchTypeFactory> {

    private String title;

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
