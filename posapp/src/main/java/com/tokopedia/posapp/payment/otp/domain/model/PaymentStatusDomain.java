package com.tokopedia.posapp.payment.otp.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentStatusDomain {
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("state")
    @Expose
    private Integer state;
//    @SerializedName("paymentDetails")
//    @Expose
//    private List<PaymentDetailDomain> paymentDetails;
    @SerializedName("items")
    @Expose
    private List<PaymentStatusItemDomain> items;
    @SerializedName("signature")
    @Expose
    private String signature;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

//    public List<PaymentDetailDomain> getPaymentDetails() {
//        return paymentDetails;
//    }
//
//    public void setPaymentDetails(List<PaymentDetailDomain> paymentDetails) {
//        this.paymentDetails = paymentDetails;
//    }

    public List<PaymentStatusItemDomain> getItems() {
        return items;
    }

    public void setItems(List<PaymentStatusItemDomain> items) {
        this.items = items;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
