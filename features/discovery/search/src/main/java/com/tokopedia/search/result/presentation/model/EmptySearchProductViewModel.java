package com.tokopedia.search.result.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class EmptySearchProductViewModel implements Visitable<ProductListTypeFactory> {

    private boolean bannerAdsAllowed = true;
    private boolean isFilterActive;

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

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
