
package com.tokopedia.core.product.model.shopscore.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopScoreDetailServiceModel {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("data")
    @Expose
    private ShopScoreDetailDataServiceModel data;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public ShopScoreDetailDataServiceModel getData() {
        return data;
    }

    public void setData(ShopScoreDetailDataServiceModel data) {
        this.data = data;
    }

}
