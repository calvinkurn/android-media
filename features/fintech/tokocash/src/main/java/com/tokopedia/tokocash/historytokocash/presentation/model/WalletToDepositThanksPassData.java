package com.tokopedia.tokocash.historytokocash.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositThanksPassData implements Parcelable {
    private WalletToDepositThanksData walletToDepositThanksData;

    private WalletToDepositThanksPassData(Builder builder) {
        setWalletToDepositThanksData(builder.walletToDepositThanksData);
    }

    public WalletToDepositThanksData getWalletToDepositThanksData() {
        return walletToDepositThanksData;
    }

    public void setWalletToDepositThanksData(WalletToDepositThanksData walletToDepositThanksData) {
        this.walletToDepositThanksData = walletToDepositThanksData;
    }


    public static final class Builder {
        private WalletToDepositThanksData walletToDepositThanksData;

        public Builder() {
        }

        public Builder walletToDepositThanksData(WalletToDepositThanksData val) {
            walletToDepositThanksData = val;
            return this;
        }

        public WalletToDepositThanksPassData build() {
            return new WalletToDepositThanksPassData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.walletToDepositThanksData, flags);
    }

    protected WalletToDepositThanksPassData(Parcel in) {
        this.walletToDepositThanksData = in.readParcelable(WalletToDepositThanksData.class.getClassLoader());
    }

    public static final Creator<WalletToDepositThanksPassData> CREATOR =
            new Creator<WalletToDepositThanksPassData>() {
                @Override
                public WalletToDepositThanksPassData createFromParcel(Parcel source) {
                    return new WalletToDepositThanksPassData(source);
                }

                @Override
                public WalletToDepositThanksPassData[] newArray(int size) {
                    return new WalletToDepositThanksPassData[size];
                }
            };
}
