package com.tokopedia.tracking.viewmodel;

import java.util.List;

/**
 * Created by kris on 5/11/18. Tokopedia
 */

public class TrackingViewModel {

    private String referenceNumber;

    private String courierLogoUrl;

    private String deliveryDate;

    private String serviceCode;

    private String sellerStore;

    private String sellerAddress;

    private String buyerName;

    private String buyerAddress;

    private int statusNumber;

    private String status;

    private List<TrackingHistoryViewModel> historyList;

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

    public List<TrackingHistoryViewModel> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<TrackingHistoryViewModel> historyList) {
        this.historyList = historyList;
    }
}
