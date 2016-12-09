package com.tokopedia.core.tracking.model.tracking;

/**
 * Created by Alifa on 10/12/2016.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackOrder {

    private static final String TAG = TrackOrder.class.getSimpleName();

    @SerializedName("change")
    @Expose
    private Integer change;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("no_history")
    @Expose
    private Integer noHistory;
    @SerializedName("track_history")
    @Expose
    private List<TrackHistory> trackHistory = new ArrayList<TrackHistory>();
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("order_status")
    @Expose
    private Integer orderStatus;
    @SerializedName("detail")
    @Expose
    private Detail detail;
    @SerializedName("shipping_ref_num")
    @Expose
    private String shippingRefNum;
    @SerializedName("invalid")
    @Expose
    private Integer invalid;

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer change) {
        this.change = change;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNoHistory() {
        return noHistory;
    }

    public void setNoHistory(Integer noHistory) {
        this.noHistory = noHistory;
    }

    public List<TrackHistory> getTrackHistory() {
        return trackHistory;
    }

    public void setTrackHistory(List<TrackHistory> trackHistory) {
        this.trackHistory = trackHistory;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public void setShippingRefNum(String shippingRefNum) {
        this.shippingRefNum = shippingRefNum;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
}