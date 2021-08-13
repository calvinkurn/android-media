package com.tokopedia.logisticorder.uimodel;

import java.util.List;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class TrackingUiModel {

    public static int ORDER_STATUS_WAITING = 501;

    private int change;

    private int noHistory;

    private String senderDate;

    private String referenceNumber;

    private String courierLogoUrl;

    private String deliveryDate;

    private String serviceCode;

    private String sellerStore;

    private String sellerAddress;

    private String buyerName;

    private String buyerAddress;

    private int statusNumber;

    private boolean isInvalid;

    private String status;

    private List<TrackingHistoryUiModel> historyList;

    private List<AdditionalInfoUiModel> additionalInfoList;

    private String trackingUrl;

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCourierLogoUrl() {
        return courierLogoUrl;
    }

    public void setCourierLogoUrl(String courierLogoUrl) {
        this.courierLogoUrl = courierLogoUrl;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getSellerStore() {
        return sellerStore;
    }

    public void setSellerStore(String sellerStore) {
        this.sellerStore = sellerStore;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TrackingHistoryUiModel> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<TrackingHistoryUiModel> historyList) {
        this.historyList = historyList;
    }

    public List<AdditionalInfoUiModel> getAdditionalInfoList() {
        return additionalInfoList;
    }

    public void setAdditionalInfoList(List<AdditionalInfoUiModel> additionalInfoList) {
        this.additionalInfoList = additionalInfoList;
    }

    public String getTrackingUrl() { return trackingUrl; }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }
}
