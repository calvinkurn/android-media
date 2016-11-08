package com.tokopedia.core.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderHistory implements Parcelable {
    private static final String TAG = OrderHistory.class.getSimpleName();

    @SerializedName("history_status_date")
    @Expose
    private String historyStatusDate;
    @SerializedName("history_status_date_full")
    @Expose
    private String historyStatusDateFull;
    @SerializedName("history_order_status")
    @Expose
    private String historyOrderStatus;
    @SerializedName("history_comments")
    @Expose
    private String historyComments;
    @SerializedName("history_action_by")
    @Expose
    private String historyActionBy;
    @SerializedName("history_buyer_status")
    @Expose
    private String historyBuyerStatus;
    @SerializedName("history_seller_status")
    @Expose
    private String historySellerStatus;

    protected OrderHistory(Parcel in) {
        historyStatusDate = in.readString();
        historyStatusDateFull = in.readString();
        historyOrderStatus = in.readString();
        historyComments = in.readString();
        historyActionBy = in.readString();
        historyBuyerStatus = in.readString();
        historySellerStatus = in.readString();
    }

    public static final Creator<OrderHistory> CREATOR = new Creator<OrderHistory>() {
        @Override
        public OrderHistory createFromParcel(Parcel in) {
            return new OrderHistory(in);
        }

        @Override
        public OrderHistory[] newArray(int size) {
            return new OrderHistory[size];
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
