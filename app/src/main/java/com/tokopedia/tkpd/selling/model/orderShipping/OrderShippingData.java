
package com.tokopedia.tkpd.selling.model.orderShipping;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderShippingData {

    @SerializedName("booking")
    @Expose
    private Booking booking;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private java.util.List<OrderShippingList> orderShippingList = new ArrayList<OrderShippingList>();

    /**
     * 
     * @return
     *     The booking
     */
    public Booking getBooking() {
        return booking;
    }

    /**
     * 
     * @param booking
     *     The booking
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    /**
     * 
     * @return
     *     The order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 
     * @return
     *     The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     * 
     * @param paging
     *     The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    /**
     * 
     * @return
     *     The orderShippingList
     */
    public java.util.List<OrderShippingList> getOrderShippingList() {
        return orderShippingList;
    }

    /**
     * 
     * @param orderShippingList
     *     The orderShippingList
     */
    public void setOrderShippingList(java.util.List<OrderShippingList> orderShippingList) {
        this.orderShippingList = orderShippingList;
    }

}
