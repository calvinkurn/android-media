
package com.tokopedia.tkpd.selling.model.orderShipping;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class OrderShippingList {

    @SerializedName("order_JOB_status")
    @Expose
    String orderJOBStatus;
    @SerializedName("order_is_pickup")
    @Expose
    int isPickUp;
    @SerializedName("order_customer")
    @Expose
    OrderCustomer orderCustomer;
    @SerializedName("order_payment")
    @Expose
    OrderPayment orderPayment;
    @SerializedName("order_detail")
    @Expose
    OrderDetail orderDetail;
    @SerializedName("order_auto_resi")
    @Expose
    String orderAutoResi;
    @SerializedName("order_deadline")
    @Expose
    OrderDeadline orderDeadline;
    @SerializedName("order_auto_awb")
    @Expose
    String orderAutoAwb;
    @SerializedName("order_shop")
    @Expose
    OrderShop orderShop;
    @SerializedName("order_products")
    @Expose
    java.util.List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
    @SerializedName("order_shipment")
    @Expose
    OrderShipment orderShipment;
    @SerializedName("order_last")
    @Expose
    OrderLast orderLast;
    @SerializedName("order_history")
    @Expose
    java.util.List<OrderHistory> orderHistory = new ArrayList<OrderHistory>();
//    @SerializedName("order_JOB_detail")
//    @Expose
//    String orderJOBDetail;
    @SerializedName("order_destination")
    @Expose
    OrderDestination orderDestination;

    /**
     * 
     * @return
     *     The orderJOBStatus
     */
    public String getOrderJOBStatus() {
        return orderJOBStatus;
    }

    /**
     * 
     * @param orderJOBStatus
     *     The order_JOB_status
     */
    public void setOrderJOBStatus(String orderJOBStatus) {
        this.orderJOBStatus = orderJOBStatus;
    }

    /**
     * 
     * @return
     *     The orderCustomer
     */
    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    /**
     * 
     * @param orderCustomer
     *     The order_customer
     */
    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    /**
     * 
     * @return
     *     The orderPayment
     */
    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    /**
     * 
     * @param orderPayment
     *     The order_payment
     */
    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }

    /**
     * 
     * @return
     *     The orderDetail
     */
    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    /**
     * 
     * @param orderDetail
     *     The order_detail
     */
    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    /**
     * 
     * @return
     *     The orderAutoResi
     */
    public String getOrderAutoResi() {
        return orderAutoResi;
    }

    /**
     * 
     * @param orderAutoResi
     *     The order_auto_resi
     */
    public void setOrderAutoResi(String orderAutoResi) {
        this.orderAutoResi = orderAutoResi;
    }

    /**
     * 
     * @return
     *     The orderDeadline
     */
    public OrderDeadline getOrderDeadline() {
        return orderDeadline;
    }

    /**
     * 
     * @param orderDeadline
     *     The order_deadline
     */
    public void setOrderDeadline(OrderDeadline orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    /**
     * 
     * @return
     *     The orderAutoAwb
     */
    public String getOrderAutoAwb() {
        return orderAutoAwb;
    }

    /**
     * 
     * @param orderAutoAwb
     *     The order_auto_awb
     */
    public void setOrderAutoAwb(String orderAutoAwb) {
        this.orderAutoAwb = orderAutoAwb;
    }

    /**
     * 
     * @return
     *     The orderShop
     */
    public OrderShop getOrderShop() {
        return orderShop;
    }

    /**
     * 
     * @param orderShop
     *     The order_shop
     */
    public void setOrderShop(OrderShop orderShop) {
        this.orderShop = orderShop;
    }

    /**
     * 
     * @return
     *     The orderProducts
     */
    public java.util.List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    /**
     * 
     * @param orderProducts
     *     The order_products
     */
    public void setOrderProducts(java.util.List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * 
     * @return
     *     The orderShipment
     */
    public OrderShipment getOrderShipment() {
        return orderShipment;
    }

    /**
     * 
     * @param orderShipment
     *     The order_shipment
     */
    public void setOrderShipment(OrderShipment orderShipment) {
        this.orderShipment = orderShipment;
    }

    /**
     * 
     * @return
     *     The orderLast
     */
    public OrderLast getOrderLast() {
        return orderLast;
    }

    /**
     * 
     * @param orderLast
     *     The order_last
     */
    public void setOrderLast(OrderLast orderLast) {
        this.orderLast = orderLast;
    }

    /**
     * 
     * @return
     *     The orderHistory
     */
    public java.util.List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    /**
     * 
     * @param orderHistory
     *     The order_history
     */
    public void setOrderHistory(java.util.List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }

//    public String getOrderJOBDetail() {
//        return orderJOBDetail;
//    }
//
//    public void setOrderJOBDetail(String orderJOBDetail) {
//        this.orderJOBDetail = orderJOBDetail;
//    }

    /**
     * 
     * @return
     *     The orderDestination
     */
    public OrderDestination getOrderDestination() {
        return orderDestination;
    }

    /**
     * 
     * @param orderDestination
     *     The order_destination
     */
    public void setOrderDestination(OrderDestination orderDestination) {
        this.orderDestination = orderDestination;
    }

    public int getIsPickUp() {
        return isPickUp;
    }

}
