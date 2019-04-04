package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class Messages {

    @SerializedName("ErrorCheckoutPriceLimit")
    @Expose
    private String errorCheckoutPriceLimit = "";
    @SerializedName("ErrorFieldBetween")
    @Expose
    private String errorFieldBetween = "";
    @SerializedName("ErrorFieldMaxChar")
    @Expose
    private String errorFieldMaxChar = "";
    @SerializedName("ErrorFieldRequired")
    @Expose
    private String errorFieldRequired = "";
    @SerializedName("ErrorProductAvailableStock")
    @Expose
    private String errorProductAvailableStock = "";
    @SerializedName("ErrorProductAvailableStockDetail")
    @Expose
    private String errorProductAvailableStockDetail = "";
    @SerializedName("ErrorProductMaxQuantity")
    @Expose
    private String errorProductMaxQuantity = "";
    @SerializedName("ErrorProductMinQuantity")
    @Expose
    private String errorProductMinQuantity = "";

    public String getErrorCheckoutPriceLimit() {
        return errorCheckoutPriceLimit;
    }

    public String getErrorFieldBetween() {
        return errorFieldBetween;
    }

    public String getErrorFieldMaxChar() {
        return errorFieldMaxChar;
    }

    public String getErrorFieldRequired() {
        return errorFieldRequired;
    }

    public String getErrorProductAvailableStock() {
        return errorProductAvailableStock;
    }

    public String getErrorProductAvailableStockDetail() {
        return errorProductAvailableStockDetail;
    }

    public String getErrorProductMaxQuantity() {
        return errorProductMaxQuantity;
    }

    public String getErrorProductMinQuantity() {
        return errorProductMinQuantity;
    }
}
