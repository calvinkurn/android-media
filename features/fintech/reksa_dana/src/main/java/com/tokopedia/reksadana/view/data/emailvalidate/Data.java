package com.tokopedia.reksadana.view.data.emailvalidate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @Expose
    @SerializedName("mf_pre_validate_email")
    private ValidateEmail mf_pre_validate_email;

    public Data(ValidateEmail mf_pre_validate_email) {
        this.mf_pre_validate_email = mf_pre_validate_email;
    }

    public ValidateEmail mf_get_sign_url() {
        return mf_pre_validate_email;
    }

    @Override
    public String toString() {
        return "Data["+
                "mf_pre_validate_email="+mf_pre_validate_email+"]";
    }
}