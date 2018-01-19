package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class AttributesRecentNumber {

    @SerializedName("client_number")
    @Expose
    private String clientNumber;

    public String getClientNumber() {
        return clientNumber;
    }
}
