package com.tokopedia.digital.wallets.accountsetting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingPassData implements Parcelable {
    private String somethingData;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.somethingData);
    }

    public WalletAccountSettingPassData() {
    }

    protected WalletAccountSettingPassData(Parcel in) {
        this.somethingData = in.readString();
    }

    public static final Parcelable.Creator<WalletAccountSettingPassData> CREATOR =
            new Parcelable.Creator<WalletAccountSettingPassData>() {
                @Override
                public WalletAccountSettingPassData createFromParcel(Parcel source) {
                    return new WalletAccountSettingPassData(source);
                }

                @Override
                public WalletAccountSettingPassData[] newArray(int size) {
                    return new WalletAccountSettingPassData[size];
                }
            };
}
