package com.tokopedia.core.network.entity.phoneverification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyPhoneNumberData {

    @SerializedName("is_verified")
    @Expose
    boolean isVerified;

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
