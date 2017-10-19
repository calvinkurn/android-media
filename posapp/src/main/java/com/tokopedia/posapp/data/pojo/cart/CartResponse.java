package com.tokopedia.posapp.data.pojo.cart;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.shopinfo.models.productmodel.List;

/**
 * Created by okasurya on 9/15/17.
 */

public class CartResponse {
    @SerializedName("id")
    private long id;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("product")
    private com.tokopedia.core.shopinfo.models.productmodel.List product;

    @SerializedName("quantity")
    private int quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List getProduct() {
        return product;
    }

    public void setProduct(List product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
