package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductTicker {

    @SerializedName("show_ticker")
    @Expose
    private boolean showTicker;
    @SerializedName("message")
    @Expose
    private String message;

    public boolean isShowTicker() {
        return showTicker;
    }

    public void setShowTicker(boolean showTicker) {
        this.showTicker = showTicker;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
