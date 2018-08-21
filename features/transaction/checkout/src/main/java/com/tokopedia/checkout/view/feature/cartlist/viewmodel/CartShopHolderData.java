package com.tokopedia.checkout.view.feature.cartlist.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopHolderData implements Parcelable {

    private ShopGroupData shopGroupData;
    private boolean selected;

    public CartShopHolderData() {
    }

    protected CartShopHolderData(Parcel in) {
        shopGroupData = in.readParcelable(ShopGroupData.class.getClassLoader());
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(shopGroupData, flags);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartShopHolderData> CREATOR = new Creator<CartShopHolderData>() {
        @Override
        public CartShopHolderData createFromParcel(Parcel in) {
            return new CartShopHolderData(in);
        }

        @Override
        public CartShopHolderData[] newArray(int size) {
            return new CartShopHolderData[size];
        }
    };

    public ShopGroupData getShopGroupData() {
        return shopGroupData;
    }

    public void setShopGroupData(ShopGroupData shopGroupData) {
        this.shopGroupData = shopGroupData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
