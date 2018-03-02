package com.tokopedia.posapp.payment.otp.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentStatusDomain {
    @SerializedName("merchantCode")
    @Expose
    private String merchantCode;
    @SerializedName("profileCode")
    @Expose
    private String profileCode;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("transactionCode")
    @Expose
    private String transactionCode;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("gatewayCode")
    @Expose
    private String gatewayCode;
    @SerializedName("gatewayType")
    @Expose
    private String gatewayType;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("additionalFee")
    @Expose
    private String additionalFee;
    @SerializedName("userDefinedValue")
    @Expose
    private String userDefinedValue;
    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("expiredOn")
    @Expose
    private Date expiredOn;
    @SerializedName("paymentDetails")
    @Expose
    private List<PaymentDetailDomain> paymentDetails;
    @SerializedName("items")
    @Expose
    private List<PaymentStatusItemDomain> items;
    @SerializedName("validParam")
    @Expose
    private String validParam;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("tokocashUsage")
    @Expose
    private String tokocashUsage;
    @SerializedName("pairData")
    @Expose
    private String pairData;
    @SerializedName("orderId")
    @Expose
    private int orderId;
    @SerializedName("invoiceRef")
    @Expose
    private String invoiceRef;
    @SerializedName("bankId")
    @Expose
    private int bankId;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("bankLogo")
    @Expose
    private String bankLogo;

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

    public List<PaymentDetailDomain> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetailDomain> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public List<PaymentStatusItemDomain> getItems() {
        return items;
    }

    public void setItems(List<PaymentStatusItemDomain> items) {
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }
}
