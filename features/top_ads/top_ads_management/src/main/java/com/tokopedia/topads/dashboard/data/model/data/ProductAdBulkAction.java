package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class ProductAdBulkAction extends BulkAction {

    @SerializedName("ads")
    @Expose
    private List<ProductAdAction> ads;

    public List<ProductAdAction> getAds() {
        return ads;
    }

    public void setAds(List<ProductAdAction> ads) {
        this.ads = ads;
    }
}
