package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 12/7/17.
 */

public class OrderData {
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("order_detail")
    @Expose
    private List<OrderDetail> orderDetail;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("shipping_price")
    @Expose
    private float shippingPrice;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public float getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(float shippingPrice) {
        this.shippingPrice = shippingPrice;
    }
}
