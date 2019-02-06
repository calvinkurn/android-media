package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by stevenfredian on 5/30/16.
 */
public class SecurityModel implements Parcelable{
    /**
     * is_login : false
     * is_register_device : 1
     * security : {"user_check_security_2":1,"allow_login":0,"user_check_security_1":0}
     * user_id : 3332764
     */

    @SerializedName("is_login")
    @Expose
    private String is_login;

    @SerializedName("is_register_device")
    @Expose
    private int is_register_device;
    /**
     * user_check_security_2 : 1
     * allow_login : 0
     * user_check_security_1 : 0
     */

    @SerializedName("security")
    @Expose
    private SecurityBean security;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    public String getIs_login() {
        return is_login;
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
    }

    public int getIs_register_device() {
        return is_register_device;
    }

    public void setIs_register_device(int is_register_device) {
        this.is_register_device = is_register_device;
    }

    public SecurityBean getSecurity() {
        return security;
    }

    public void setSecurity(SecurityBean security) {
        this.security = security;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public static class SecurityBean implements Parcelable {

        @SerializedName("user_check_security_2")
        @Expose
        private int user_check_security_2;

        @SerializedName("allow_login")
        @Expose
        private int allow_login;

        @SerializedName("user_check_security_1")
        @Expose
        private int user_check_security_1;

        public int getUser_check_security_2() {
            return user_check_security_2;
        }

        public void setUser_check_security_2(int user_check_security_2) {
            this.user_check_security_2 = user_check_security_2;
        }

        public int getAllow_login() {
            return allow_login;
        }

        public void setAllow_login(int allow_login) {
            this.allow_login = allow_login;
        }

        public int getUser_check_security_1() {
            return user_check_security_1;
        }

        public void setUser_check_security_1(int user_check_security_1) {
            this.user_check_security_1 = user_check_security_1;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.user_check_security_2);
            dest.writeInt(this.allow_login);
            dest.writeInt(this.user_check_security_1);
        }

        public SecurityBean() {
        }

        protected SecurityBean(Parcel in) {
            this.user_check_security_2 = in.readInt();
            this.allow_login = in.readInt();
            this.user_check_security_1 = in.readInt();
        }

        public static final Creator<SecurityBean> CREATOR = new Creator<SecurityBean>() {
            @Override
            public SecurityBean createFromParcel(Parcel source) {
                return new SecurityBean(source);
            }

            @Override
            public SecurityBean[] newArray(int size) {
                return new SecurityBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.is_login);
        dest.writeInt(this.is_register_device);
        dest.writeParcelable(this.security, flags);
        dest.writeInt(this.user_id);
    }

    public SecurityModel() {
    }

    protected SecurityModel(Parcel in) {
        this.is_login = in.readString();
        this.is_register_device = in.readInt();
        this.security = in.readParcelable(SecurityBean.class.getClassLoader());
        this.user_id = in.readInt();
    }

    public static final Creator<SecurityModel> CREATOR = new Creator<SecurityModel>() {
        @Override
        public SecurityModel createFromParcel(Parcel source) {
            return new SecurityModel(source);
        }

        @Override
        public SecurityModel[] newArray(int size) {
            return new SecurityModel[size];
        }
    };
}
