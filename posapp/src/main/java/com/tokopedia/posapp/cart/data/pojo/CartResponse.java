package com.tokopedia.posapp.cart.data.pojo;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.posapp.product.common.data.pojo.ProductDetail;

/**
 * Created by okasurya on 9/15/17.
 */

public class CartResponse {
    @SerializedName("id")
    private long id;

    @SerializedName("product_id")
    private long productId;

    @SerializedName("product")
    private ProductDetail product;

    @SerializedName("quantity")
    private int quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public ProductDetail getProduct() {
        return product;
    }

    public void setProduct(ProductDetail product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
