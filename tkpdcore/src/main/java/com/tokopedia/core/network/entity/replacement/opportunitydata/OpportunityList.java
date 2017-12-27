
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpportunityList {

    @SerializedName("order_replacement_id")
    @Expose
    private int orderReplacementId;
    @SerializedName("order_order_id")
    @Expose
    private int orderOrderId;
    @SerializedName("order_payment_at")
    @Expose
    private String orderPaymentAt;
    @SerializedName("order_expired_at")
    @Expose
    private String orderExpiredAt;
    @SerializedName("order_cashback_idr")
    @Expose
    private String orderCashbackIdr;
    @SerializedName("order_cashback")
    @Expose
    private String orderCashback;
    @SerializedName("order_customer")
    @Expose
    private OrderCustomer orderCustomer;
    @SerializedName("order_payment")
    @Expose
    private OrderPayment orderPayment;
    @SerializedName("order_detail")
    @Expose
    private OrderDetail orderDetail;
    @SerializedName("order_deadline")
    @Expose
    private OrderDeadline orderDeadline;
    @SerializedName("order_shop")
    @Expose
    private OrderShop orderShop;
    @SerializedName("order_products")
    @Expose
    private List<OrderProduct> orderProducts = null;
    @SerializedName("order_shipment")
    @Expose
    private OrderShipment orderShipment;
    @SerializedName("order_last")
    @Expose
    private OrderLast orderLast;
    @SerializedName("order_history")
    @Expose
    private List<OrderHistory> orderHistory = null;
    @SerializedName("order_destination")
    @Expose
    private OrderDestination orderDestination;
    @SerializedName("replacement_multiplier_value")
    @Expose
    private int replacementMultiplierValue;
    @SerializedName("replacement_multiplier_value_str")
    @Expose
    private String replacementMultiplierValueStr;
    @SerializedName("replacement_multiplier_color")
    @Expose
    private String replacementMultiplierColor;

    // replacement URL for webview to show the terms and condition before the product being taken
    @SerializedName("replacement_tnc")
    @Expose
    private String replacementTnc;

    public int getOrderReplacementId() {
        return orderReplacementId;
    }

    public int getOrderOrderId() {
        return orderOrderId;
    }

    public String getOrderPaymentAt() {
        return orderPaymentAt;
    }

    public String getOrderExpiredAt() {
        return orderExpiredAt;
    }

    public String getOrderCashbackIdr() {
        return orderCashbackIdr;
    }

    public String getOrderCashback() {
        return orderCashback;
    }

    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public OrderDeadline getOrderDeadline() {
        return orderDeadline;
    }

    public OrderShop getOrderShop() {
        return orderShop;
    }

    public java.util.List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public OrderShipment getOrderShipment() {
        return orderShipment;
    }

    public OrderLast getOrderLast() {
        return orderLast;
    }

    public java.util.List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public String getReplacementTnc() {
        return replacementTnc;
    }

    public OrderDestination getOrderDestination() {
        return orderDestination;
    }

    public int getReplacementMultiplierValue() {
        return replacementMultiplierValue;
    }

    public String getReplacementMultiplierValueStr() {
        return replacementMultiplierValueStr;
    }

    public String getReplacementMultiplierColor() {
        return replacementMultiplierColor;
    }
}
