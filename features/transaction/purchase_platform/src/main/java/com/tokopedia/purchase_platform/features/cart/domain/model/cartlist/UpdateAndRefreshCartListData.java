package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Irfan Khoirul on 09/09/18.
 */

public class UpdateAndRefreshCartListData implements Parcelable {
    private UpdateCartData updateCartData;
    private CartListData cartListData;

    public UpdateCartData getUpdateCartData() {
        return updateCartData;
    }

    public void setUpdateCartData(UpdateCartData updateCartData) {
        this.updateCartData = updateCartData;
    }

    public CartListData getCartListData() {
        return cartListData;
    }

    public void setCartListData(CartListData cartListData) {
        this.cartListData = cartListData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.updateCartData, flags);
        dest.writeParcelable(this.cartListData, flags);
    }

    public UpdateAndRefreshCartListData() {
    }

    protected UpdateAndRefreshCartListData(Parcel in) {
        this.updateCartData = in.readParcelable(UpdateCartData.class.getClassLoader());
        this.cartListData = in.readParcelable(CartListData.class.getClassLoader());
    }

    public static final Creator<UpdateAndRefreshCartListData> CREATOR = new Creator<UpdateAndRefreshCartListData>() {
        @Override
        public UpdateAndRefreshCartListData createFromParcel(Parcel source) {
            return new UpdateAndRefreshCartListData(source);
        }

        @Override
        public UpdateAndRefreshCartListData[] newArray(int size) {
            return new UpdateAndRefreshCartListData[size];
        }
    };
}
