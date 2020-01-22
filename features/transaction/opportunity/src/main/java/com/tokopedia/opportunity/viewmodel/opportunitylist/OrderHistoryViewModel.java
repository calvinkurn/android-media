package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/21/17.
 */

public class OrderHistoryViewModel implements Parcelable{

    private String historyStatusDate;
    private String historyStatusDateFull;
    private String historyOrderStatus;
    private String historyComments;
    private String historyActionBy;
    private String historyBuyerStatus;
    private String historySellerStatus;

    public OrderHistoryViewModel() {
    }

    protected OrderHistoryViewModel(Parcel in) {
        historyStatusDate = in.readString();
        historyStatusDateFull = in.readString();
        historyOrderStatus = in.readString();
        historyComments = in.readString();
        historyActionBy = in.readString();
        historyBuyerStatus = in.readString();
        historySellerStatus = in.readString();
    }

    public static final Creator<OrderHistoryViewModel> CREATOR = new Creator<OrderHistoryViewModel>() {
        @Override
        public OrderHistoryViewModel createFromParcel(Parcel in) {
            return new OrderHistoryViewModel(in);
        }

        @Override
        public OrderHistoryViewModel[] newArray(int size) {
            return new OrderHistoryViewModel[size];
        }
    };

    public String getHistoryStatusDate() {
        return historyStatusDate;
    }

    public void setHistoryStatusDate(String historyStatusDate) {
        this.historyStatusDate = historyStatusDate;
    }

    public String getHistoryStatusDateFull() {
        return historyStatusDateFull;
    }

    public void setHistoryStatusDateFull(String historyStatusDateFull) {
        this.historyStatusDateFull = historyStatusDateFull;
    }

    public String getHistoryOrderStatus() {
        return historyOrderStatus;
    }

    public void setHistoryOrderStatus(String historyOrderStatus) {
        this.historyOrderStatus = historyOrderStatus;
    }

    public String getHistoryComments() {
        return historyComments;
    }

    public void setHistoryComments(String historyComments) {
        this.historyComments = historyComments;
    }

    public String getHistoryActionBy() {
        return historyActionBy;
    }

    public void setHistoryActionBy(String historyActionBy) {
        this.historyActionBy = historyActionBy;
    }

    public String getHistoryBuyerStatus() {
        return historyBuyerStatus;
    }

    public void setHistoryBuyerStatus(String historyBuyerStatus) {
        this.historyBuyerStatus = historyBuyerStatus;
    }

    public String getHistorySellerStatus() {
        return historySellerStatus;
    }

    public void setHistorySellerStatus(String historySellerStatus) {
        this.historySellerStatus = historySellerStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(historyStatusDate);
        dest.writeString(historyStatusDateFull);
        dest.writeString(historyOrderStatus);
        dest.writeString(historyComments);
        dest.writeString(historyActionBy);
        dest.writeString(historyBuyerStatus);
        dest.writeString(historySellerStatus);
    }
}
