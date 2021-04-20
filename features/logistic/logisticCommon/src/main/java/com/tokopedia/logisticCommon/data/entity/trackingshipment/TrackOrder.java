package com.tokopedia.logisticCommon.data.entity.trackingshipment;

/**
 * Created by Alifa on 10/12/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrackOrder {

    @SerializedName("change")
    @Expose
    private int change;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("no_history")
    @Expose
    private int noHistory;
    @SerializedName("track_history")
    @Expose
    private List<TrackHistory> trackHistory = new ArrayList<TrackHistory>();
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("detail")
    @Expose
    private Detail detail;
    @SerializedName("shipping_ref_num")
    @Expose
    private String shippingRefNum;
    @SerializedName("invalid")
    @Expose
    private int invalid;

    public int getChange() {
        return change;
    }

    public String getStatus() {
        return status;
    }

    public int getNoHistory() {
        return noHistory;
    }

    public List<TrackHistory> getTrackHistory() {
        return trackHistory;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public Detail getDetail() {
        return detail;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public int getInvalid() {
        return invalid;
    }
}