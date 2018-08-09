package com.tokopedia.train.checkout.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutParameterEntity {

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

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;

    @SerializedName("customerMsisdn")
    @Expose
    private String customerMsisdn;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("itemsName")
    @Expose
    private List<String> itemsName;

    @SerializedName("itemsQuantity")
    @Expose
    private List<String> itemsQuantity;

    @SerializedName("itemsPrice")
    @Expose
    private List<String> itemsPrice;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("signature")
    @Expose
    private String signature;

    @SerializedName("userDefinedValue")
    @Expose
    private String userDefinedValue;

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerMsisdn() {
        return customerMsisdn;
    }

    public String getCurrency() {
        return currency;
    }

    public List<String> getItemsName() {
        return itemsName;
    }

    public List<String> getItemsQuantity() {
        return itemsQuantity;
    }

    public List<String> getItemsPrice() {
        return itemsPrice;
    }

    public String getAmount() {
        return amount;
    }

    public String getSignature() {
        return signature;
    }

    public String getUserDefinedValue() {
        return userDefinedValue;
    }
}