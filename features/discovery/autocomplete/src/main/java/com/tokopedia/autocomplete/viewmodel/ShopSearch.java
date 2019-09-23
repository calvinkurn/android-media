package com.tokopedia.autocomplete.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.autocomplete.adapter.SearchTypeFactory;

public class ShopSearch extends BaseItemAutoCompleteSearch
        implements Visitable<SearchTypeFactory> {

    private String location;
    private boolean isOfficial;
    private String shopBadgeIconUrl;

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

    public void setShopBadgeIconUrl(String shopBadgeIconUrl) {
        this.shopBadgeIconUrl = shopBadgeIconUrl;
    }

    public String getShopBadgeIconUrl() {
        return this.shopBadgeIconUrl;
    }
}
