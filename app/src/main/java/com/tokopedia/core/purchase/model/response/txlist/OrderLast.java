package com.tokopedia.core.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderLast implements Parcelable {
    private static final String TAG = OrderLast.class.getSimpleName();

    @SerializedName("last_order_id")
    @Expose
    private String lastOrderId;
    @SerializedName("last_shipment_id")
    @Expose
    private String lastShipmentId;
    @SerializedName("last_est_shipping_left")
    @Expose
    private String lastEstShippingLeft;
    @SerializedName("last_order_status")
    @Expose
    private String lastOrderStatus;
    @SerializedName("last_status_date")
    @Expose
    private String lastStatusDate;
    @SerializedName("last_pod_code")
    @Expose
    private String lastPodCode;
    @SerializedName("last_pod_desc")
    @Expose
    private String lastPodDesc;
    @SerializedName("last_shipping_ref_num")
    @Expose
    private String lastShippingRefNum;
    @SerializedName("last_pod_receiver")
    @Expose
    private String lastPodReceiver;
    @SerializedName("last_comments")
    @Expose
    private String lastComments;
    @SerializedName("last_buyer_status")
    @Expose
    private String lastBuyerStatus;
    @SerializedName("last_status_date_wib")
    @Expose
    private String lastStatusDateWib;
    @SerializedName("last_seller_status")
    @Expose
    private String lastSellerStatus;

    protected OrderLast(Parcel in) {
        lastOrderId = in.readString();
        lastShipmentId = in.readString();
        lastEstShippingLeft = in.readString();
        lastOrderStatus = in.readString();
        lastStatusDate = in.readString();
        lastPodCode = in.readString();
        lastPodDesc = in.readString();
        lastShippingRefNum = in.readString();
        lastPodReceiver = in.readString();
        lastComments = in.readString();
        lastBuyerStatus = in.readString();
        lastStatusDateWib = in.readString();
        lastSellerStatus = in.readString();
    }

    public static final Creator<OrderLast> CREATOR = new Creator<OrderLast>() {
        @Override
        public OrderLast createFromParcel(Parcel in) {
            return new OrderLast(in);
        }

        @Override
        public OrderLast[] newArray(int size) {
            return new OrderLast[size];
        }
    };

    public String getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(String lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    public String getLastShipmentId() {
        return lastShipmentId;
    }

    public void setLastShipmentId(String lastShipmentId) {
        this.lastShipmentId = lastShipmentId;
    }

    public String getLastEstShippingLeft() {
        return lastEstShippingLeft;
    }

    public void setLastEstShippingLeft(String lastEstShippingLeft) {
        this.lastEstShippingLeft = lastEstShippingLeft;
    }

    public String getLastOrderStatus() {
        return lastOrderStatus;
    }

    public void setLastOrderStatus(String lastOrderStatus) {
        this.lastOrderStatus = lastOrderStatus;
    }

    public String getLastStatusDate() {
        return lastStatusDate;
    }

    public void setLastStatusDate(String lastStatusDate) {
        this.lastStatusDate = lastStatusDate;
    }

    public String getLastPodCode() {
        return lastPodCode;
    }

    public void setLastPodCode(String lastPodCode) {
        this.lastPodCode = lastPodCode;
    }

    public String getLastPodDesc() {
        return lastPodDesc;
    }

    public void setLastPodDesc(String lastPodDesc) {
        this.lastPodDesc = lastPodDesc;
    }

    public String getLastShippingRefNum() {
        return lastShippingRefNum;
    }

    public void setLastShippingRefNum(String lastShippingRefNum) {
        this.lastShippingRefNum = lastShippingRefNum;
    }

    public String getLastPodReceiver() {
        return lastPodReceiver;
    }

    public void setLastPodReceiver(String lastPodReceiver) {
        this.lastPodReceiver = lastPodReceiver;
    }

    public String getLastComments() {
        return lastComments;
    }

    public void setLastComments(String lastComments) {
        this.lastComments = lastComments;
    }

    public String getLastBuyerStatus() {
        return lastBuyerStatus;
    }

    public void setLastBuyerStatus(String lastBuyerStatus) {
        this.lastBuyerStatus = lastBuyerStatus;
    }

    public String getLastStatusDateWib() {
        return lastStatusDateWib;
    }

    public void setLastStatusDateWib(String lastStatusDateWib) {
        this.lastStatusDateWib = lastStatusDateWib;
    }

    public String getLastSellerStatus() {
        return lastSellerStatus;
    }

    public void setLastSellerStatus(String lastSellerStatus) {
        this.lastSellerStatus = lastSellerStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastOrderId);
        dest.writeString(lastShipmentId);
        dest.writeString(lastEstShippingLeft);
        dest.writeString(lastOrderStatus);
        dest.writeString(lastStatusDate);
        dest.writeString(lastPodCode);
        dest.writeString(lastPodDesc);
        dest.writeString(lastShippingRefNum);
        dest.writeString(lastPodReceiver);
        dest.writeString(lastComments);
        dest.writeString(lastBuyerStatus);
        dest.writeString(lastStatusDateWib);
        dest.writeString(lastSellerStatus);
    }
}
