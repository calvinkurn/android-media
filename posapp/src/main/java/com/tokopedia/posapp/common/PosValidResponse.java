package com.tokopedia.posapp.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 5/24/18.
 */
public class PosValidResponse {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid() {
        if(status.toLowerCase().contains("error")) {
            return false;
        }

        return true;
    }
}
