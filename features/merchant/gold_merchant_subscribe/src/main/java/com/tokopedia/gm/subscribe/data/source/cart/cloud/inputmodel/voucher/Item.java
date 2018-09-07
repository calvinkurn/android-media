package com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class Item {
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("qty")
    @Expose
    private Integer qty;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
