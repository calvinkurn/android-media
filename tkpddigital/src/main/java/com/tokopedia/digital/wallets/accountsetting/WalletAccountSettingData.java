package com.tokopedia.digital.wallets.accountsetting;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingData implements Parcelable {

    private String subTitle;
    private String name;
    private String email;
    private String phoneNumber;
    private List<WalletAccountSettingConnectedUserData> connectedUserDataList = new ArrayList<>();

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<WalletAccountSettingConnectedUserData> getConnectedUserDataList() {
        return connectedUserDataList;
    }

    public void setConnectedUserDataList(List<WalletAccountSettingConnectedUserData> connectedUserDataList) {
        this.connectedUserDataList = connectedUserDataList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subTitle);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.phoneNumber);
        dest.writeTypedList(this.connectedUserDataList);
    }

    public WalletAccountSettingData() {
    }

    protected WalletAccountSettingData(Parcel in) {
        this.subTitle = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.connectedUserDataList = in.createTypedArrayList(WalletAccountSettingConnectedUserData.CREATOR);
    }

    public static final Parcelable.Creator<WalletAccountSettingData> CREATOR =
            new Parcelable.Creator<WalletAccountSettingData>() {
                @Override
                public WalletAccountSettingData createFromParcel(Parcel source) {
                    return new WalletAccountSettingData(source);
                }

                @Override
                public WalletAccountSettingData[] newArray(int size) {
                    return new WalletAccountSettingData[size];
                }
            };
}
