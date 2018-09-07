package com.tokopedia.checkout.view.view.shipment.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.view.view.shipment.ShipmentData;

/**
 * @author Irfan Khoirul on 14/05/18.
 */

public class ShipmentCheckoutButtonModel implements ShipmentData, Parcelable {

    private String totalPayment;
    private boolean ableToCheckout;
    private boolean hideBottomShadow;

    public ShipmentCheckoutButtonModel() {
    }

    protected ShipmentCheckoutButtonModel(Parcel in) {
        totalPayment = in.readString();
        ableToCheckout = in.readByte() != 0;
        hideBottomShadow = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(totalPayment);
        dest.writeByte((byte) (ableToCheckout ? 1 : 0));
        dest.writeByte((byte) (hideBottomShadow ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentCheckoutButtonModel> CREATOR = new Creator<ShipmentCheckoutButtonModel>() {
        @Override
        public ShipmentCheckoutButtonModel createFromParcel(Parcel in) {
            return new ShipmentCheckoutButtonModel(in);
        }

        @Override
        public ShipmentCheckoutButtonModel[] newArray(int size) {
            return new ShipmentCheckoutButtonModel[size];
        }
    };

    public boolean isAbleToCheckout() {
        return ableToCheckout;
    }

    public void setAbleToCheckout(boolean ableToCheckout) {
        this.ableToCheckout = ableToCheckout;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public boolean isHideBottomShadow() {
        return hideBottomShadow;
    }

    public void setHideBottomShadow(boolean hideBottomShadow) {
        this.hideBottomShadow = hideBottomShadow;
    }
}
