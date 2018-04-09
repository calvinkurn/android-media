package com.tokopedia.posapp.product.management.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/9/18.
 */

public class ProductPriceRequest {
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("stock")
    @Expose
    private int stock;
    @SerializedName("status")
    @Expose
    private int status;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
