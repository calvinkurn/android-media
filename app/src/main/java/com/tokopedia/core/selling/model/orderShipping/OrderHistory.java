
package com.tokopedia.core.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderHistory {

    @SerializedName("history_status_date")
    @Expose
    String historyStatusDate;
    @SerializedName("history_status_date_full")
    @Expose
    String historyStatusDateFull;
    @SerializedName("history_order_status")
    @Expose
    String historyOrderStatus;
    @SerializedName("history_comments")
    @Expose
    String historyComments;
    @SerializedName("history_action_by")
    @Expose
    String historyActionBy;
    @SerializedName("history_buyer_status")
    @Expose
    String historyBuyerStatus;
    @SerializedName("history_seller_status")
    @Expose
    String historySellerStatus;

    /**
     * 
     * @return
     *     The historyStatusDate
     */
    public String getHistoryStatusDate() {
        return historyStatusDate;
    }

    /**
     * 
     * @param historyStatusDate
     *     The history_status_date
     */
    public void setHistoryStatusDate(String historyStatusDate) {
        this.historyStatusDate = historyStatusDate;
    }

    /**
     * 
     * @return
     *     The historyStatusDateFull
     */
    public String getHistoryStatusDateFull() {
        return historyStatusDateFull;
    }

    /**
     * 
     * @param historyStatusDateFull
     *     The history_status_date_full
     */
    public void setHistoryStatusDateFull(String historyStatusDateFull) {
        this.historyStatusDateFull = historyStatusDateFull;
    }

    /**
     * 
     * @return
     *     The historyOrderStatus
     */
    public String getHistoryOrderStatus() {
        return historyOrderStatus;
    }

    /**
     * 
     * @param historyOrderStatus
     *     The history_order_status
     */
    public void setHistoryOrderStatus(String historyOrderStatus) {
        this.historyOrderStatus = historyOrderStatus;
    }

    /**
     * 
     * @return
     *     The historyComments
     */
    public String getHistoryComments() {
        return historyComments;
    }

    /**
     * 
     * @param historyComments
     *     The history_comments
     */
    public void setHistoryComments(String historyComments) {
        this.historyComments = historyComments;
    }

    /**
     * 
     * @return
     *     The historyActionBy
     */
    public String getHistoryActionBy() {
        return historyActionBy;
    }

    /**
     * 
     * @param historyActionBy
     *     The history_action_by
     */
    public void setHistoryActionBy(String historyActionBy) {
        this.historyActionBy = historyActionBy;
    }

    /**
     * 
     * @return
     *     The historyBuyerStatus
     */
    public String getHistoryBuyerStatus() {
        return historyBuyerStatus;
    }

    /**
     * 
     * @param historyBuyerStatus
     *     The history_buyer_status
     */
    public void setHistoryBuyerStatus(String historyBuyerStatus) {
        this.historyBuyerStatus = historyBuyerStatus;
    }

    /**
     * 
     * @return
     *     The historySellerStatus
     */
    public String getHistorySellerStatus() {
        return historySellerStatus;
    }

    /**
     * 
     * @param historySellerStatus
     *     The history_seller_status
     */
    public void setHistorySellerStatus(String historySellerStatus) {
        this.historySellerStatus = historySellerStatus;
    }

}
