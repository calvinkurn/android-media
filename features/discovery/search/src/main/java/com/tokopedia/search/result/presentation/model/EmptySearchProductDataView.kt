package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class EmptySearchProductDataView implements Visitable<ProductListTypeFactory> {

    private boolean bannerAdsAllowed = true;
    private boolean isFilterActive = false;
    private boolean isLocalSearch = false;
    private String globalSearchApplink = "";
    private String keyword = "";
    private String pageTitle = "";

    public boolean isBannerAdsAllowed() {
        return bannerAdsAllowed;
    }

    public void setBannerAdsAllowed(boolean bannerAdsAllowed) {
        this.bannerAdsAllowed = bannerAdsAllowed;
    }

    public void setIsFilterActive(boolean isFilterActive) {
        this.isFilterActive = isFilterActive;
    }

    public boolean getIsFilterActive() {
        return isFilterActive;
    }

    public boolean isLocalSearch() {
        return isLocalSearch;
    }

    public void setLocalSearch(boolean localSearch) {
        isLocalSearch = localSearch;
    }

    public String getGlobalSearchApplink() {
        return globalSearchApplink;
    }

    public void setGlobalSearchApplink(String globalSearchApplink) {
        this.globalSearchApplink = globalSearchApplink;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
