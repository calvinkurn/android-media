package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 1/19/18.
 */

public class OrderDetail {
    @SerializedName("order_detail_id")
    @Expose
    private int orderDetailId;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_price")
    @Expose
    private float productPrice;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("subtotal_price")
    @Expose
    private float subtotalPrice;


    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(float subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }
}
