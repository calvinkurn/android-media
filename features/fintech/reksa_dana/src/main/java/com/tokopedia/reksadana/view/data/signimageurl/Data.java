package com.tokopedia.reksadana.view.data.signimageurl;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @Expose
    @SerializedName("mf_get_sign_url")
    private GetSignUrl mf_get_sign_url;

    public Data(GetSignUrl mf_get_sign_url) {
        this.mf_get_sign_url = mf_get_sign_url;
    }

    public GetSignUrl mf_get_sign_url() {
        return mf_get_sign_url;
    }
}
