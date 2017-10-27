package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class OAuthInfo implements Parcelable {

    private String userId;
    private String email;
    private String name;
    private String msisdn;

    public OAuthInfo() {
    }

    protected OAuthInfo(Parcel in) {
        userId = in.readString();
        email = in.readString();
        name = in.readString();
        msisdn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(msisdn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OAuthInfo> CREATOR = new Creator<OAuthInfo>() {
        @Override
        public OAuthInfo createFromParcel(Parcel in) {
            return new OAuthInfo(in);
        }

        @Override
        public OAuthInfo[] newArray(int size) {
            return new OAuthInfo[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
