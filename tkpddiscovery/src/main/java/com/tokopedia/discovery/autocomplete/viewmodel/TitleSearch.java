package com.tokopedia.discovery.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

public class TitleSearch implements Visitable<SearchTypeFactory> {

    private boolean visible;
    private String title;

    public TitleSearch() {
        this.visible = false;
    }

    public TitleSearch(boolean visible) {
        this.visible = visible;
    }

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
