package com.tokopedia.discovery.autocomplete.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

public class ShopSearch extends BaseItemAutoCompleteSearch
        implements Visitable<SearchTypeFactory> {

    private String location;
    private boolean isOfficial;

    @Override
    public int type(SearchTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

}
