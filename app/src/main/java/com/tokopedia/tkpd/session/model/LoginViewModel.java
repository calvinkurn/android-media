package com.tokopedia.tkpd.session.model;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class LoginViewModel {
    String username;
    String password;
    String uuid;
    boolean isProgressShow;
    boolean isEmailClick;
    boolean isGoogleClick;
    boolean isFacebookClick;

    public static final int USERNAME = -999;
    public static final int PASSWORD = -998;
    public static final int ISPROGRESSSHOW = -997;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isProgressShow() {
        return isProgressShow;
    }

    public void setIsProgressShow(boolean isProgressShow) {
        this.isProgressShow = isProgressShow;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isEmailClick() {
        return isEmailClick;
    }

    public void setIsEmailClick(boolean isEmailClick) {
        this.isEmailClick = isEmailClick;
    }

    public boolean isGoogleClick() {
        return isGoogleClick;
    }

    public void setIsGoogleClick(boolean isGoogleClick) {
        this.isGoogleClick = isGoogleClick;
    }

    public boolean isFacebookClick() {
        return isFacebookClick;
    }

    public void setIsFacebookClick(boolean isFacebookClick) {
        this.isFacebookClick = isFacebookClick;
    }

    @Override
    public String toString() {
        return "LoginViewModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uuid='" + uuid + '\'' +
                ", isProgressShow=" + isProgressShow +
                '}';
    }
}
