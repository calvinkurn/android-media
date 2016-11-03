package com.tokopedia.tkpd.session.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 18/11/2015.
 */
@Parcel
public class ForgotPasswordModel {

    @SerializedName("url_redirect")
    @Expose
    String urlRedirect;
    @SerializedName("is_success")
    @Expose
    int isSuccess;

    public String getUrlRedirect() {
        return urlRedirect;
    }

    public void setUrlRedirect(String urlRedirect) {
        this.urlRedirect = urlRedirect;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public String toString() {
        return "ForgotPasswordModel{" +
                "urlRedirect='" + urlRedirect + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
