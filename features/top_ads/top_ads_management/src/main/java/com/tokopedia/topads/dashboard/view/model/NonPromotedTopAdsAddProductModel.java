package com.tokopedia.topads.dashboard.view.model;

import com.tokopedia.base.list.seller.common.util.ItemType;

/**
 * @author normansyahputa on 3/6/17.
 */

public class NonPromotedTopAdsAddProductModel
        implements BaseTopAdsProductModel, ItemType {
    public static final int TYPE = 718192;

    public String description;
    public String snippet;
    public TopAdsProductViewModel productDomain;

    public NonPromotedTopAdsAddProductModel(String description, String snippet
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
        return getProductDomain();
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
