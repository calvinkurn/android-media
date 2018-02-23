package com.tokopedia.posapp.auth.validatepassword.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 9/28/17.
 */

public class ValidatePasswordResponse {

    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }
}
