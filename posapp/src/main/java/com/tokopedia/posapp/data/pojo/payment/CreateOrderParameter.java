package com.tokopedia.posapp.data.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 10/11/17.
 */

public class CreateOrderParameter {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("cart")
    @Expose
    private OrderCartParameter cart;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("gateway_code")
    @Expose
    private String gatewayCode;
    @SerializedName("user_defined_string")
    @Expose
    private String userDefinedString;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;
    @SerializedName("currency")
    @Expose
    private String currency;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OrderCartParameter getCart() {
        return cart;
    }

    public void setCart(OrderCartParameter cart) {
        this.cart = cart;
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

    public String getUserDefinedString() {
        return userDefinedString;
    }

    public void setUserDefinedString(String userDefinedString) {
        this.userDefinedString = userDefinedString;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
