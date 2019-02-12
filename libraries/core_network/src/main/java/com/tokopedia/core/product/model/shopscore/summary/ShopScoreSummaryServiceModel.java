
package com.tokopedia.core.product.model.shopscore.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ShopScoreSummaryServiceModel {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("data")
    @Expose
    private Data data;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
