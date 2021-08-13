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
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_price")
    @Expose
    private double productPrice;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("subtotal_price")
    @Expose
    private float subtotalPrice;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
