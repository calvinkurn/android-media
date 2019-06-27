package com.tokopedia.transactiondata.entity.response.deletecart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class DeleteCartDataResponse {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message = "";

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
