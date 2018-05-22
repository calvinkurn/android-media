package com.tokopedia.events.data.entity.response.checkoutreponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Parameter {

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("transaction_date")
    private String transactionDate;

    @SerializedName("items[price]")
    private List<Integer> itemsPrice;

    @SerializedName("transaction_code")
    private String transactionCode;

    @SerializedName("merchant_code")
    private String merchantCode;

    @SerializedName("amount")
    private String amount;

    @SerializedName("user_defined_value")
    private String userDefinedValue;

    @SerializedName("signature")
    private String signature;

    @SerializedName("fee")
    private String fee;

    @SerializedName("profile_code")
    private String profileCode;

    @SerializedName("nid")
    private String nid;

    @SerializedName("language")
    private String language;

    @SerializedName("items[name]")
    private List<String> itemsName;

    @SerializedName("customer_email")
    private String customerEmail;

    @SerializedName("items[quantity]")
    private List<Integer> itemsQuantity;

    @SerializedName("currency")
    private String currency;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("state")
    private int state;

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setItemsPrice(List<Integer> itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public List<Integer> getItemsPrice() {
        return itemsPrice;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setUserDefinedValue(String userDefinedValue) {
        this.userDefinedValue = userDefinedValue;
    }

    public String getUserDefinedValue() {
        return userDefinedValue;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFee() {
        return fee;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNid() {
        return nid;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setItemsName(List<String> itemsName) {
        this.itemsName = itemsName;
    }

    public List<String> getItemsName() {
        return itemsName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setItemsQuantity(List<Integer> itemsQuantity) {
        this.itemsQuantity = itemsQuantity;
    }

    public List<Integer> getItemsQuantity() {
        return itemsQuantity;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        return
                "Parameter{" +
                        "transaction_id = '" + transactionId + '\'' +
                        ",transaction_date = '" + transactionDate + '\'' +
                        ",items[price] = '" + itemsPrice + '\'' +
                        ",transaction_code = '" + transactionCode + '\'' +
                        ",merchant_code = '" + merchantCode + '\'' +
                        ",amount = '" + amount + '\'' +
                        ",user_defined_value = '" + userDefinedValue + '\'' +
                        ",signature = '" + signature + '\'' +
                        ",fee = '" + fee + '\'' +
                        ",profile_code = '" + profileCode + '\'' +
                        ",nid = '" + nid + '\'' +
                        ",language = '" + language + '\'' +
                        ",items[name] = '" + itemsName + '\'' +
                        ",customer_email = '" + customerEmail + '\'' +
                        ",items[quantity] = '" + itemsQuantity + '\'' +
                        ",currency = '" + currency + '\'' +
                        ",customer_name = '" + customerName + '\'' +
                        ",state = '" + state + '\'' +
                        "}";
    }
}