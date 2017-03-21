
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderLast {

    @SerializedName("last_order_id")
    @Expose
    private int lastOrderId;
    @SerializedName("last_shipment_id")
    @Expose
    private String lastShipmentId;
    @SerializedName("last_est_shipping_left")
    @Expose
    private int lastEstShippingLeft;
    @SerializedName("last_order_status")
    @Expose
    private String lastOrderStatus;
    @SerializedName("last_status_date")
    @Expose
    private String lastStatusDate;
    @SerializedName("last_pod_code")
    @Expose
    private int lastPodCode;
    @SerializedName("last_pod_desc")
    @Expose
    private String lastPodDesc;
    @SerializedName("last_shipping_ref_num")
    @Expose
    private String lastShippingRefNum;
    @SerializedName("last_pod_receiver")
    @Expose
    private int lastPodReceiver;
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

    public int getLastEstShippingLeft() {
        return lastEstShippingLeft;
    }

    public void setLastEstShippingLeft(int lastEstShippingLeft) {
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

    public int getLastPodCode() {
        return lastPodCode;
    }

    public void setLastPodCode(int lastPodCode) {
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

    public int getLastPodReceiver() {
        return lastPodReceiver;
    }

    public void setLastPodReceiver(int lastPodReceiver) {
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

}
