
package com.tokopedia.otp.cotp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateOtpPojo {

    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("uuid")
    @Expose
    String uuid;

    public int getIsSuccess() {
        return isSuccess;
    }

    public String getUuid() {
        return uuid;
    }
}
