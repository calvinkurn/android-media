package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.affiliate.common.data.pojo.AffiliateCheckPojo;

/**
 * @author by yfsx on 12/10/18.
 */
public class ExploreFirstQuery {

    @SerializedName("topadsExploreAffiliateProduct")
    @Expose
    private ExploreQuery exploreProduct;

    @SerializedName("affiliateCheck")
    @Expose
    private AffiliateCheckPojo affiliateCheck;

    public ExploreQuery getExploreProduct() {
        return exploreProduct;
    }

    public void setExploreProduct(ExploreQuery exploreProduct) {
        this.exploreProduct = exploreProduct;
    }

    public AffiliateCheckPojo getAffiliateCheck() {
        return affiliateCheck;
    }

    public void setAffiliateCheck(AffiliateCheckPojo affiliateCheck) {
        this.affiliateCheck = affiliateCheck;
    }
}
