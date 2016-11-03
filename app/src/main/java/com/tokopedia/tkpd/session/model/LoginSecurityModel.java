package com.tokopedia.tkpd.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class LoginSecurityModel {
    @SerializedName("is_login")
    boolean isLogin;
    @SerializedName("is_register_device")
    int isRegisterDevice;
    @SerializedName("user_id")
    int userId;

    SecurityQuestion securityQuestion;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    @Parcel
    public static class SecurityQuestion{
        @SerializedName("allow_login")
        int allowLogin;
        @SerializedName("user_check_security_1")
        int userCheckSecurity1;
        @SerializedName("user_check_security_2")
        int userCheckSecurity2;

        public int getUserCheckSecurity2() {
            return userCheckSecurity2;
        }

        public void setUserCheckSecurity2(int userCheckSecurity2) {
            this.userCheckSecurity2 = userCheckSecurity2;
        }

        public int getUserCheckSecurity1() {
            return userCheckSecurity1;
        }

        public void setUserCheckSecurity1(int userCheckSecurity1) {
            this.userCheckSecurity1 = userCheckSecurity1;
        }

        public int getAllowLogin() {
            return allowLogin;
        }

        public void setAllowLogin(int allowLogin) {
            this.allowLogin = allowLogin;
        }
    }
}
