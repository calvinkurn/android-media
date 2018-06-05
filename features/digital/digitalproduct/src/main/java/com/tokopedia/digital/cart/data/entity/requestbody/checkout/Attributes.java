package com.tokopedia.digital.cart.data.entity.requestbody.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.utils.data.RequestBodyAppsFlyer;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class Attributes {

    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("transaction_amount")
    @Expose
    private Long transactionAmount;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;
    @SerializedName("appsflyer")
    @Expose
    private RequestBodyAppsFlyer appsFlyer;
    @SerializedName("client_id")
    @Expose
    private String clientId;

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setTransactionAmount(Long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAppsFlyer(RequestBodyAppsFlyer appsFlyer) {
        this.appsFlyer = appsFlyer;
    }
}
