package com.tokopedia.loginregister.activation.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/17/17.
 */

public class ActivateUnicodePojo {

    @SerializedName("error")
    @Expose
    String error;

    @SerializedName("error_description")
    @Expose
    String errorDescription;

    @SerializedName("access_token")
    @Expose
    String accessToken;

    @SerializedName("expires_in")
    @Expose
    String expiresIn;

    @SerializedName("token_type")
    @Expose
    String tokenType;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
