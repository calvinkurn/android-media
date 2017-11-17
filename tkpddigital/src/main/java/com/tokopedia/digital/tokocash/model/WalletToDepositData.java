package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositData implements Parcelable {

    private String title;
    private String subTitle;
    private String nominalFormatted;
    private String titleButtonNegative;
    private String titleButtonPositive;

    private WalletToDepositData(Builder builder) {
        setTitle(builder.title);
        setSubTitle(builder.subTitle);
        setNominalFormatted(builder.nominalFormatted);
        setTitleButtonNegative(builder.titleButtonNegative);
        setTitleButtonPositive(builder.titleButtonPositive);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNominalFormatted() {
        return nominalFormatted;
    }

    public void setNominalFormatted(String nominalFormatted) {
        this.nominalFormatted = nominalFormatted;
    }

    public String getTitleButtonNegative() {
        return titleButtonNegative;
    }

    public void setTitleButtonNegative(String titleButtonNegative) {
        this.titleButtonNegative = titleButtonNegative;
    }

    public String getTitleButtonPositive() {
        return titleButtonPositive;
    }

    public void setTitleButtonPositive(String titleButtonPositive) {
        this.titleButtonPositive = titleButtonPositive;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.nominalFormatted);
        dest.writeString(this.titleButtonNegative);
        dest.writeString(this.titleButtonPositive);
    }

    public WalletToDepositData() {
    }

    protected WalletToDepositData(Parcel in) {
        this.title = in.readString();
        this.subTitle = in.readString();
        this.nominalFormatted = in.readString();
        this.titleButtonNegative = in.readString();
        this.titleButtonPositive = in.readString();
    }

    public static final Creator<WalletToDepositData> CREATOR
            = new Creator<WalletToDepositData>() {
        @Override
        public WalletToDepositData createFromParcel(Parcel source) {
            return new WalletToDepositData(source);
        }

        @Override
        public WalletToDepositData[] newArray(int size) {
            return new WalletToDepositData[size];
        }
    };


    public static final class Builder {
        private String title;
        private String subTitle;
        private String nominalFormatted;
        private String titleButtonNegative;
        private String titleButtonPositive;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder subTitle(String val) {
            subTitle = val;
            return this;
        }

        public Builder nominalFormatted(String val) {
            nominalFormatted = val;
            return this;
        }

        public Builder titleButtonNegative(String val) {
            titleButtonNegative = val;
            return this;
        }

        public Builder titleButtonPositive(String val) {
            titleButtonPositive = val;
            return this;
        }

        public WalletToDepositData build() {
            return new WalletToDepositData(this);
        }
    }
}
