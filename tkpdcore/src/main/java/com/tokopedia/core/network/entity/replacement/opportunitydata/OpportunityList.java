
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

    public int getOrderReplacementId() {
        return orderReplacementId;
    }

    public void setOrderReplacementId(int orderReplacementId) {
        this.orderReplacementId = orderReplacementId;
    }

    public int getOrderOrderId() {
        return orderOrderId;
    }

    public void setOrderOrderId(int orderOrderId) {
        this.orderOrderId = orderOrderId;
    }

    public String getOrderPaymentAt() {
        return orderPaymentAt;
    }

    public void setOrderPaymentAt(String orderPaymentAt) {
        this.orderPaymentAt = orderPaymentAt;
    }

    public String getOrderExpiredAt() {
        return orderExpiredAt;
    }

    public void setOrderExpiredAt(String orderExpiredAt) {
        this.orderExpiredAt = orderExpiredAt;
    }

    public String getOrderCashbackIdr() {
        return orderCashbackIdr;
    }

    public void setOrderCashbackIdr(String orderCashbackIdr) {
        this.orderCashbackIdr = orderCashbackIdr;
    }

    public String getOrderCashback() {
        return orderCashback;
    }

    public void setOrderCashback(String orderCashback) {
        this.orderCashback = orderCashback;
    }

    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderDeadline getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(OrderDeadline orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public OrderShop getOrderShop() {
        return orderShop;
    }

    public void setOrderShop(OrderShop orderShop) {
        this.orderShop = orderShop;
    }

    public java.util.List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(java.util.List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderShipment getOrderShipment() {
        return orderShipment;
    }

    public void setOrderShipment(OrderShipment orderShipment) {
        this.orderShipment = orderShipment;
    }

    public OrderLast getOrderLast() {
        return orderLast;
    }

    public void setOrderLast(OrderLast orderLast) {
        this.orderLast = orderLast;
    }

    public java.util.List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(java.util.List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public OrderDestination getOrderDestination() {
        return orderDestination;
    }

    public void setOrderDestination(OrderDestination orderDestination) {
        this.orderDestination = orderDestination;
    }

}
