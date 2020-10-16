package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 12/7/17.
 */

public class OrderData {
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("order_info")
    @Expose
    private OrderInfo orderInfo;
    @SerializedName("shop_info")
    @Expose
    private Shop shop;
    @SerializedName("item_price")
    @Expose
    private float itemPrice;
    @SerializedName("is_fulfillment")
    @Expose
    private boolean isFulfillment;
    @SerializedName("is_new_buyer")
    @Expose
    private boolean isNewBuyer;
    @SerializedName("is_free_shipping")
    @Expose
    private boolean isFreeShipping;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public void setFulfillment(boolean fulfillment) {
        isFulfillment = fulfillment;
    }

    public boolean isNewBuyer() {
        return isNewBuyer;
    }

    public void setNewBuyer(boolean newBuyer) {
        isNewBuyer = newBuyer;
    }

    public boolean isFreeShipping() {
        return isFreeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        isFreeShipping = freeShipping;
    }
}
