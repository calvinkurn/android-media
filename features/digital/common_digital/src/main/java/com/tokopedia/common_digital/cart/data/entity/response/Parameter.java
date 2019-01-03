package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class Parameter {
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
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("items[name]")
    @Expose
    private List<String> itemsName = null;
    @SerializedName("items[quantity]")
    @Expose
    private List<Integer> itemsQuantity = null;
    @SerializedName("items[price]")
    @Expose
    private List<Integer> itemsPrice = null;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("user_defined_value")
    @Expose
    private String userDefinedValue;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("state")
    @Expose
    private int state;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("payments[amount]")
    @Expose
    private List<String> paymentsAmount = null;
    @SerializedName("payments[name]")
    @Expose
    private List<String> paymentsName = null;

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

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public List<String> getItemsName() {
        return itemsName;
    }

    public List<Integer> getItemsQuantity() {
        return itemsQuantity;
    }

    public List<Integer> getItemsPrice() {
        return itemsPrice;
    }

    public String getSignature() {
        return signature;
    }

    public String getLanguage() {
        return language;
    }

    public String getUserDefinedValue() {
        return userDefinedValue;
    }

    public String getNid() {
        return nid;
    }

    public int getState() {
        return state;
    }

    public String getFee() {
        return fee;
    }

    public List<String> getPaymentsAmount() {
        return paymentsAmount;
    }

    public List<String> getPaymentsName() {
        return paymentsName;
    }
}
