package com.tokopedia.core.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 10/11/2015.
 */
@Parcel
public class LoginInterruptErrorModel {
    @SerializedName("is_login")
    boolean isLogin;
    @SerializedName("error")
    int error;
    @SerializedName("allow_login")
    int allowLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(int allowLogin) {
        this.allowLogin = allowLogin;
    }
}
