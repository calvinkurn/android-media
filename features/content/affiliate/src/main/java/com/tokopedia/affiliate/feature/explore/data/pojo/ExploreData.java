package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/10/18.
 */
public class ExploreData {
    @SerializedName("topadsExploreAffiliateProduct")
    @Expose
    private ExploreProduct exploreProduct;

    public ExploreProduct getExploreProduct() {
        return exploreProduct;
    }

    public void setExploreProduct(ExploreProduct exploreProduct) {
        this.exploreProduct = exploreProduct;
    }
}
