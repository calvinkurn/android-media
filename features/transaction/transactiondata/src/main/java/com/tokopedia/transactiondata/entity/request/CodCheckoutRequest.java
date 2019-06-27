package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class CodCheckoutRequest {

    @SerializedName("client_id")
    @Expose
    private String clientId;

    @SerializedName("profile")
    @Expose
    private String profile;

    @SerializedName("fingerprint_support")
    @Expose
    private String fingerPrintSupport;

    @SerializedName("fingerprint_publickey")
    @Expose
    private String fingerPrintPublicKey;

    @SerializedName("carts")
    @Expose
    private CheckoutRequest carts;

    @SerializedName("optional")
    @Expose
    private Integer optional;

    @SerializedName("is_one_click_shipment")
    @Expose
    private String isOneClickShipment;

    public CodCheckoutRequest() {
    }

    public CodCheckoutRequest(String clientId, String profile, String fingerPrintSupport, String fingerPrintPublicKey, CheckoutRequest carts, Integer optional, String isOneClickShipment) {
        this.clientId = clientId;
        this.profile = profile;
        this.fingerPrintSupport = fingerPrintSupport;
        this.fingerPrintPublicKey = fingerPrintPublicKey;
        this.carts = carts;
        this.optional = optional;
        this.isOneClickShipment = isOneClickShipment;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFingerPrintSupport() {
        return fingerPrintSupport;
    }

    public void setFingerPrintSupport(String fingerPrintSupport) {
        this.fingerPrintSupport = fingerPrintSupport;
    }

    public String getFingerPrintPublicKey() {
        return fingerPrintPublicKey;
    }

    public void setFingerPrintPublicKey(String fingerPrintPublicKey) {
        this.fingerPrintPublicKey = fingerPrintPublicKey;
    }

    public CheckoutRequest getCarts() {
        return carts;
    }

    public void setCarts(CheckoutRequest carts) {
        this.carts = carts;
    }

    public Integer getOptional() {
        return optional;
    }

    public void setOptional(Integer optional) {
        this.optional = optional;
    }

    public String getIsOneClickShipment() {
        return isOneClickShipment;
    }

    public void setIsOneClickShipment(String isOneClickShipment) {
        this.isOneClickShipment = isOneClickShipment;
    }
}
