package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 10/27/17.
 */

public class AccountTokoCash implements Parcelable {

    private String clientId;
    private String identifier;
    private String imgUrl;
    private String authDateFmt;

    public AccountTokoCash() {
    }

    protected AccountTokoCash(Parcel in) {
        clientId = in.readString();
        identifier = in.readString();
        imgUrl = in.readString();
        authDateFmt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientId);
        dest.writeString(identifier);
        dest.writeString(imgUrl);
        dest.writeString(authDateFmt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountTokoCash> CREATOR = new Creator<AccountTokoCash>() {
        @Override
        public AccountTokoCash createFromParcel(Parcel in) {
            return new AccountTokoCash(in);
        }

        @Override
        public AccountTokoCash[] newArray(int size) {
            return new AccountTokoCash[size];
        }
    };

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAuthDateFmt() {
        return authDateFmt;
    }

    public void setAuthDateFmt(String authDateFmt) {
        this.authDateFmt = authDateFmt;
    }
}
