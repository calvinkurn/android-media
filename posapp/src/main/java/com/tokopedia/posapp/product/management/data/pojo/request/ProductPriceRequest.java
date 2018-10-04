package com.tokopedia.posapp.product.management.data.pojo.request;

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
    private int price;
    @SerializedName("stock")
    @Expose
    private int stock;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("etalase_id")
    @Expose
    private long etalaseId;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }
}
