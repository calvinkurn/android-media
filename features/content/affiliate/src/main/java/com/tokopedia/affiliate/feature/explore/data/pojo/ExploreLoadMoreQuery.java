package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by milhamj on 10/16/18.
 */
public class ExploreLoadMoreQuery {
    @SerializedName("topadsExploreAffiliateProduct")
    @Expose
    private ExploreQuery exploreProduct;

    public ExploreQuery getExploreProduct() {
        return exploreProduct;
    }

    public void setExploreProduct(ExploreQuery exploreProduct) {
        this.exploreProduct = exploreProduct;
    }
}
