package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class OAuthInfo implements Parcelable {

    private List<AccountWalletItem> accountList;
    private String tokopediaUserId;
    private String email;
    private String name;
    private String mobile;

    public OAuthInfo() {
    }

    protected OAuthInfo(Parcel in) {
        tokopediaUserId = in.readString();
        email = in.readString();
        name = in.readString();
        mobile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokopediaUserId);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(mobile);
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

    public List<AccountWalletItem> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountWalletItem> accountList) {
        this.accountList = accountList;
    }

    public String getTokopediaUserId() {
        return tokopediaUserId;
    }

    public void setTokopediaUserId(String tokopediaUserId) {
        this.tokopediaUserId = tokopediaUserId;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
