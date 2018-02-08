package com.tokopedia.digital.widget.data.entity.recentnumber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class AttributesEntity {

    @SerializedName("client_number")
    @Expose
    private String clientNumber;

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }
}
