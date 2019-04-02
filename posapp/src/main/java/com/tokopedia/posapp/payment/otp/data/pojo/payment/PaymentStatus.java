package com.tokopedia.posapp.payment.otp.data.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentStatus {
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("transaction_code")
    @Expose
    private String transactionCode;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("gateway_code")
    @Expose
    private String gatewayCode;
    @SerializedName("gateway_type")
    @Expose
    private String gatewayType;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("additional_fee")
    @Expose
    private String additionalFee;
    @SerializedName("user_defined_value")
    @Expose
    private String userDefinedValue;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("expired_on")
    @Expose
    private Date expiredOn;
    @SerializedName("payment_details")
    @Expose
    private List<PaymentDetail> paymentDetails;
    @SerializedName("items")
    @Expose
    private List<PaymentStatusItem> items;
    @SerializedName("valid_param")
    @Expose
    private String validParam;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("tokocash_usage")
    @Expose
    private String tokocashUsage;
    @SerializedName("pair_data")
    @Expose
    private String pairData;
    @SerializedName("error_code")
    @Expose
    private String errorCode;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(String additionalFee) {
        this.additionalFee = additionalFee;
    }

    public String getUserDefinedValue() {
        return userDefinedValue;
    }

    public void setUserDefinedValue(String userDefinedValue) {
        this.userDefinedValue = userDefinedValue;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(Date expiredOn) {
        this.expiredOn = expiredOn;
    }

    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public List<PaymentStatusItem> getItems() {
        return items;
    }

    public void setItems(List<PaymentStatusItem> items) {
        this.items = items;
    }

    public String getValidParam() {
        return validParam;
    }

    public void setValidParam(String validParam) {
        this.validParam = validParam;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTokocashUsage() {
        return tokocashUsage;
    }

    public void setTokocashUsage(String tokocashUsage) {
        this.tokocashUsage = tokocashUsage;
    }

    public String getPairData() {
        return pairData;
    }

    public void setPairData(String pairData) {
        this.pairData = pairData;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
