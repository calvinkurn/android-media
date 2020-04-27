package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashwanityagi on 03/07/17.
 */

public class PulsaBalance implements Parcelable {
    private String mobileNumber;
    private String pulsaBalance;
    private String plainBalance;
    private String expireDate;
    private boolean success;


    private PulsaBalance(Builder builder) {
        setMobileNumber(builder.mobileNumber);
        setPulsaBalance(builder.mobileBalance);
        setPlainBalance(builder.plainBalance);
        setPlainBalance(builder.expireDate);
        setSuccess(builder.success);

    }

    public String getPlainBalance() {
        return plainBalance;
    }

    public void setPlainBalance(String plainBalance) {
        this.plainBalance = plainBalance;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPulsaBalance() {
        return pulsaBalance;
    }

    public void setPulsaBalance(String pulsaBalance) {
        this.pulsaBalance = pulsaBalance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PulsaBalance() {
    }


    public static final class Builder {
        private String mobileNumber;
        private String mobileBalance;
        private String plainBalance;
        private String expireDate;
        private boolean success;


        public Builder() {
        }

        public Builder mobileNumber(String val) {
            mobileNumber = val;
            return this;
        }

        public Builder mobileBalance(String val) {
            mobileBalance = val;
            return this;
        }

        public Builder plainBalance(String val) {
            plainBalance = val;
            return this;
        }

        public Builder expireDate(String val) {
            expireDate = val;
            return this;
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }




        public PulsaBalance build() {
            return new PulsaBalance(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mobileNumber);
        dest.writeString(this.pulsaBalance);
        dest.writeString(this.plainBalance);
        dest.writeString(this.expireDate);
    }

    protected PulsaBalance(Parcel in) {
        this.mobileNumber = in.readString();
        this.pulsaBalance = in.readString();
        this.plainBalance = in.readString();
        this.expireDate = in.readString();
    }

    public static final Creator<PulsaBalance> CREATOR = new Creator<PulsaBalance>() {
        @Override
        public PulsaBalance createFromParcel(Parcel source) {
            return new PulsaBalance(source);
        }

        @Override
        public PulsaBalance[] newArray(int size) {
            return new PulsaBalance[size];
        }
    };
}