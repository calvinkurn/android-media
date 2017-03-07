
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("order_JOB_status")
    @Expose
    private int orderJOBStatus;
    @SerializedName("order_is_pickup")
    @Expose
    private int orderIsPickup;
    @SerializedName("order_shipping_retry")
    @Expose
    private int orderShippingRetry;
    @SerializedName("order_customer")
    @Expose
    private OrderCustomer orderCustomer;
    @SerializedName("order_payment")
    @Expose
    private Object orderPayment;
    @SerializedName("order_detail")
    @Expose
    private OrderDetail orderDetail;
    @SerializedName("order_auto_resi")
    @Expose
    private String orderAutoResi;
    @SerializedName("order_deadline")
    @Expose
    private OrderDeadline orderDeadline;
    @SerializedName("order_auto_awb")
    @Expose
    private int orderAutoAwb;
    @SerializedName("order_shop")
    @Expose
    private OrderShop orderShop;
    @SerializedName("order_products")
    @Expose
    private java.util.List<OrderProduct> orderProducts = null;
    @SerializedName("order_shipment")
    @Expose
    private OrderShipment orderShipment;
    @SerializedName("order_last")
    @Expose
    private Object orderLast;
    @SerializedName("order_history")
    @Expose
    private Object orderHistory;
    @SerializedName("order_JOB_detail")
    @Expose
    private Object orderJOBDetail;
    @SerializedName("order_destination")
    @Expose
    private OrderDestination orderDestination;

    public int getOrderJOBStatus() {
        return orderJOBStatus;
    }

    public void setOrderJOBStatus(int orderJOBStatus) {
        this.orderJOBStatus = orderJOBStatus;
    }

    public int getOrderIsPickup() {
        return orderIsPickup;
    }

    public void setOrderIsPickup(int orderIsPickup) {
        this.orderIsPickup = orderIsPickup;
    }

    public int getOrderShippingRetry() {
        return orderShippingRetry;
    }

    public void setOrderShippingRetry(int orderShippingRetry) {
        this.orderShippingRetry = orderShippingRetry;
    }

    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public Object getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(Object orderPayment) {
        this.orderPayment = orderPayment;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderAutoResi() {
        return orderAutoResi;
    }

    public void setOrderAutoResi(String orderAutoResi) {
        this.orderAutoResi = orderAutoResi;
    }

    public OrderDeadline getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(OrderDeadline orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public int getOrderAutoAwb() {
        return orderAutoAwb;
    }

    public void setOrderAutoAwb(int orderAutoAwb) {
        this.orderAutoAwb = orderAutoAwb;
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

    public Object getOrderLast() {
        return orderLast;
    }

    public void setOrderLast(Object orderLast) {
        this.orderLast = orderLast;
    }

    public Object getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(Object orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Object getOrderJOBDetail() {
        return orderJOBDetail;
    }

    public void setOrderJOBDetail(Object orderJOBDetail) {
        this.orderJOBDetail = orderJOBDetail;
    }

    public OrderDestination getOrderDestination() {
        return orderDestination;
    }

    public void setOrderDestination(OrderDestination orderDestination) {
        this.orderDestination = orderDestination;
    }

}
