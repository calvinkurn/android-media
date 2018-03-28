package com.tokopedia.payment.fingerprint.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class DataResponseSavePublicKey {
    @SerializedName("is_success")
    @Expose
    private int success;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
