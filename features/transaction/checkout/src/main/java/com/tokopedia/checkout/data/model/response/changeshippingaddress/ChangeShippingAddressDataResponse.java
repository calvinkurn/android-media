package com.tokopedia.checkout.data.model.response.changeshippingaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class ChangeShippingAddressDataResponse {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private List<String> messages;

    public int getSuccess() {
        return success;
    }

    public List<String> getMessages() {
        return messages;
    }
}
