
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpportunityData {

    @SerializedName("booking")
    @Expose
    private Object booking;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private List<OpportunityList> opportunityList = null;

    public Object getBooking() {
        return booking;
    }

    public void setBooking(Object booking) {
        this.booking = booking;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<OpportunityList> getOpportunityList() {
        return opportunityList;
    }

    public void setOpportunityList(List<OpportunityList> opportunityList) {
        this.opportunityList = opportunityList;
    }

}
