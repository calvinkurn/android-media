package com.tokopedia.common_digital.cart.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

public class CartAdditionalInfo implements Parcelable, Visitable {

    private String title;

    private List<CartItemDigital> cartItemDigitalList;

    public CartAdditionalInfo(String title, List<CartItemDigital> cartItemDigitalList) {
        this.title = title;
        this.cartItemDigitalList = cartItemDigitalList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CartItemDigital> getCartItemDigitalList() {
        return cartItemDigitalList;
    }

    public void setCartItemDigitalList(List<CartItemDigital> cartItemDigitalList) {
        this.cartItemDigitalList = cartItemDigitalList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeList(this.cartItemDigitalList);
    }

    protected CartAdditionalInfo(Parcel in) {
        this.title = in.readString();
        this.cartItemDigitalList = new ArrayList<CartItemDigital>();
        in.readList(this.cartItemDigitalList, CartItemDigital.class.getClassLoader());
    }

    public static final Creator<CartAdditionalInfo> CREATOR =
            new Creator<CartAdditionalInfo>() {
                @Override
                public CartAdditionalInfo createFromParcel(Parcel source) {
                    return new CartAdditionalInfo(source);
                }

                @Override
                public CartAdditionalInfo[] newArray(int size) {
                    return new CartAdditionalInfo[size];
                }
            };

    @Override
    public int type(Object typeFactory) {
        return 0;
    }
}
