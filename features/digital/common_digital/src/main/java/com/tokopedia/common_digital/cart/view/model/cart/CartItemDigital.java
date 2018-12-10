package com.tokopedia.common_digital.cart.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * @author by Nabilla Sabbaha on 2/28/2017.
 */
public class CartItemDigital implements Parcelable, Visitable {

    private String label;

    private String value;

    public CartItemDigital(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.value);
    }

    protected CartItemDigital(Parcel in) {
        this.label = in.readString();
        this.value = in.readString();
    }

    public static final Creator<CartItemDigital> CREATOR =
            new Creator<CartItemDigital>() {
                @Override
                public CartItemDigital createFromParcel(Parcel source) {
                    return new CartItemDigital(source);
                }

                @Override
                public CartItemDigital[] newArray(int size) {
                    return new CartItemDigital[size];
                }
            };

    @Override
    public int type(Object typeFactory) {
        return 0;
    }
}
