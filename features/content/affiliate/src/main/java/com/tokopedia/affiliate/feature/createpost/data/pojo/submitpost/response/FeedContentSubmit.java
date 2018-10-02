
package com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedContentSubmit {

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("redirectURI")
    @Expose
    private String redirectURI;
    @SerializedName("error")
    @Expose
    private String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
