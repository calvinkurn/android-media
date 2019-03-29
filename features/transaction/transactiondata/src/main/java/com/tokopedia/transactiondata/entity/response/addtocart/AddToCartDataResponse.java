package com.tokopedia.transactiondata.entity.response.addtocart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class AddToCartDataResponse {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private List<String> message = new ArrayList<>();
    @SerializedName("data")
    @Expose
    private Data data;

    public int getSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    public List<String> getMessage() {
        return message;
    }
}
