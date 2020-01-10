package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/21/17.
 */

public class OrderLastViewModel implements Parcelable{
    private int lastOrderId;
    private String lastShipmentId;
    private String lastEstShippingLeft;
    private String lastOrderStatus;
    private String lastStatusDate;
    private String lastPodCode;
    private String lastPodDesc;
    private String lastShippingRefNum;
    private String lastPodReceiver;
    private String lastComments;
    private String lastBuyerStatus;
    private String lastStatusDateWib;
    private String lastSellerStatus;

    public OrderLastViewModel() {
    }

    protected OrderLastViewModel(Parcel in) {
        lastOrderId = in.readInt();
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

    public static final Creator<OrderLastViewModel> CREATOR = new Creator<OrderLastViewModel>() {
        @Override
        public OrderLastViewModel createFromParcel(Parcel in) {
            return new OrderLastViewModel(in);
        }

        @Override
        public OrderLastViewModel[] newArray(int size) {
            return new OrderLastViewModel[size];
        }
    };

    public int getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(int lastOrderId) {
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
        dest.writeInt(lastOrderId);
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
