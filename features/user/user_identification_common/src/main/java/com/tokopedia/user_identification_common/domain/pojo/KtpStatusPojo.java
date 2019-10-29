package com.tokopedia.user_identification_common.domain.pojo;
//
// Created by Yoris Prayogo on 2019-10-29.
//

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KtpStatusPojo {

    @Expose
    @SerializedName("isKtp")
    private Boolean isValid = false;

    @Expose
    @SerializedName("bypass")
    private Boolean bypass = false;

    @Expose
    @SerializedName("error")
    private String error;


    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Boolean getBypass() {
        return bypass;
    }

    public void setBypass(Boolean bypass) {
        this.bypass = bypass;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
