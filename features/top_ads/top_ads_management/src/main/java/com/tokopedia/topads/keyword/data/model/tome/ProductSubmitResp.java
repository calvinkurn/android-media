package com.tokopedia.topads.keyword.data.model.tome;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSubmitResp {
    @SerializedName("product_id")
    @Expose
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
