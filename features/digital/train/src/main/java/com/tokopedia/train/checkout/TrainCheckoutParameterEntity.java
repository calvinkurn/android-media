package com.tokopedia.train.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 26/07/18.
 */
class TrainCheckoutParameterEntity {

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

}