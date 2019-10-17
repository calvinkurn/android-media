package com.tokopedia.purchase_platform.features.checkout.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class SaveShipmentStateResponse {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;

    public int getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
