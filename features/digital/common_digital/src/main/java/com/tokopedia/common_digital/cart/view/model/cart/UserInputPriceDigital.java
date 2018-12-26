package com.tokopedia.common_digital.cart.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

public class UserInputPriceDigital implements Parcelable {

    private String minPayment;

    private String maxPayment;

    private long minPaymentPlain;

    private long maxPaymentPlain;

    public String getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(String minPayment) {
        this.minPayment = minPayment;
    }

    public String getMaxPayment() {
        return maxPayment;
    }

    public void setMaxPayment(String maxPayment) {
        this.maxPayment = maxPayment;
    }

    public long getMinPaymentPlain() {
        return minPaymentPlain;
    }

    public void setMinPaymentPlain(long minPaymentPlain) {
        this.minPaymentPlain = minPaymentPlain;
    }

    public long getMaxPaymentPlain() {
        return maxPaymentPlain;
    }

    public void setMaxPaymentPlain(long maxPaymentPlain) {
        this.maxPaymentPlain = maxPaymentPlain;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.minPayment);
        dest.writeString(this.maxPayment);
        dest.writeLong(this.minPaymentPlain);
        dest.writeLong(this.maxPaymentPlain);
    }

    public UserInputPriceDigital() {
    }

    protected UserInputPriceDigital(Parcel in) {
        this.minPayment = in.readString();
        this.maxPayment = in.readString();
        this.minPaymentPlain = in.readLong();
        this.maxPaymentPlain = in.readLong();
    }

    public static final Creator<UserInputPriceDigital> CREATOR =
            new Creator<UserInputPriceDigital>() {
                @Override
                public UserInputPriceDigital createFromParcel(Parcel source) {
                    return new UserInputPriceDigital(source);
                }

                @Override
                public UserInputPriceDigital[] newArray(int size) {
                    return new UserInputPriceDigital[size];
                }
            };
}
