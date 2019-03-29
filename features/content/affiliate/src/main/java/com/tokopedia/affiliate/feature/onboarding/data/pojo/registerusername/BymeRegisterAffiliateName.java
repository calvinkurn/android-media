
package com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BymeRegisterAffiliateName {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("error")
    @Expose
    private Error error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
