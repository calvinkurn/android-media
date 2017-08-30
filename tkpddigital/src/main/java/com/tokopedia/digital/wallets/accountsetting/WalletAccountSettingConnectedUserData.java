package com.tokopedia.digital.wallets.accountsetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/29/17.
 */

public class WalletAccountSettingConnectedUserData implements Parcelable {

    private String email;
    private String userName;
    private String lastConnected;
    private String iconUrl;
    private int resIconId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(String lastConnected) {
        this.lastConnected = lastConnected;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getResIconId() {
        return resIconId;
    }

    public void setResIconId(int resIconId) {
        this.resIconId = resIconId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.userName);
        dest.writeString(this.lastConnected);
        dest.writeString(this.iconUrl);
        dest.writeInt(this.resIconId);
    }

    public WalletAccountSettingConnectedUserData() {
    }

    protected WalletAccountSettingConnectedUserData(Parcel in) {
        this.email = in.readString();
        this.userName = in.readString();
        this.lastConnected = in.readString();
        this.iconUrl = in.readString();
        this.resIconId = in.readInt();
    }

    public static final Parcelable.Creator<WalletAccountSettingConnectedUserData> CREATOR =
            new Parcelable.Creator<WalletAccountSettingConnectedUserData>() {
                @Override
                public WalletAccountSettingConnectedUserData createFromParcel(Parcel source) {
                    return new WalletAccountSettingConnectedUserData(source);
                }

                @Override
                public WalletAccountSettingConnectedUserData[] newArray(int size) {
                    return new WalletAccountSettingConnectedUserData[size];
                }
            };
}
