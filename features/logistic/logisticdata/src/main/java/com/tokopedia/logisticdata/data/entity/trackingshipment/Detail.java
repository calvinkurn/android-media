package com.tokopedia.logisticdata.data.entity.trackingshipment;

/**
 * Created by Alifa on 10/12/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    private static final String TAG = Detail.class.getSimpleName();

    @SerializedName("shipper_city")
    @Expose
    private String shipperCity;
    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("receiver_city")
    @Expose
    private String receiverCity;
    @SerializedName("send_date")
    @Expose
    private String sendDate;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("service_code")
    @Expose
    private String serviceCode;

    public String getShipperCity() {
        return shipperCity;
    }

    public void setShipperCity(String shipperCity) {
        this.shipperCity = shipperCity;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

}
