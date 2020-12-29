package com.tokopedia.checkout.data.model.response.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class Parameter {

    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("gateway_code")
    @Expose
    private String gatewayCode;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("user_defined_value")
    @Expose
    private String userDefinedValue;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("device_info")
    @Expose
    private DeviceInfo deviceInfo;
    @SerializedName("payment_metadata")
    @Expose
    private String paymentMetadata;

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public String getPid() {
        return pid;
    }

    public String getNid() {
        return nid;
    }

    public String getUserDefinedValue() {
        return userDefinedValue;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLanguage() {
        return language;
    }

    public String getSignature() {
        return signature;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public String getPaymentMetadata() {
        return paymentMetadata;
    }
}
