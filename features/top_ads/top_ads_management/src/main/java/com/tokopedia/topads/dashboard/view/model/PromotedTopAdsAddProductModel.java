package com.tokopedia.topads.dashboard.view.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 3/6/17.
 */

public class PromotedTopAdsAddProductModel
        implements BaseTopAdsProductModel, ItemType {
    public static final int TYPE = 129130;

    public String description;
    public String snippet;
    public TopAdsProductViewModel productDomain;

    public PromotedTopAdsAddProductModel(String description, String snippet
            , TopAdsProductViewModel productDomain) {
        this.description = description;
        this.snippet = snippet;
        this.productDomain = productDomain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public TopAdsProductViewModel getProductDomain() {
        return productDomain;
    }

    public void setProductDomain(TopAdsProductViewModel productDomain) {
        this.productDomain = productDomain;
    }

    @Override
    public TopAdsProductViewModel getTopAdsProductViewModel() {
        return productDomain;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
