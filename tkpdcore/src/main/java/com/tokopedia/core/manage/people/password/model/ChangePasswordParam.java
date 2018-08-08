package com.tokopedia.core.manage.people.password.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stevenfredian on 9/28/16.
 */
public class ChangePasswordParam implements Parcelable{

    static String PARAM_OLD = "password";
    static String PARAM_NEW = "new_password";
    static String PARAM_CONFIRM = "confirm_password";


    String oldPassword;
    String newPassword;
    String confPassword;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.oldPassword);
        dest.writeString(this.newPassword);
        dest.writeString(this.confPassword);
    }

    public ChangePasswordParam() {
    }

    protected ChangePasswordParam(Parcel in) {
        this.oldPassword = in.readString();
        this.newPassword = in.readString();
        this.confPassword = in.readString();
    }

    public static final Creator<ChangePasswordParam> CREATOR = new Creator<ChangePasswordParam>() {
        @Override
        public ChangePasswordParam createFromParcel(Parcel source) {
            return new ChangePasswordParam(source);
        }

        @Override
        public ChangePasswordParam[] newArray(int size) {
            return new ChangePasswordParam[size];
        }
    };


    public Map<String, String> getParamChangePassword() {
        Map<String,String> map = new HashMap<>();
        map.put(PARAM_OLD, getOldPassword());
        map.put(PARAM_NEW, getNewPassword());
        map.put(PARAM_CONFIRM, getConfPassword());
        return map;
    }
}
