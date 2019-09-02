package com.tokopedia.purchase_platform.features.cart.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupData;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopHolderData implements Parcelable {

    private ShopGroupData shopGroupData;
    private boolean allSelected;
    private boolean partialSelected;

    public CartShopHolderData() {
    }

    protected CartShopHolderData(Parcel in) {
        shopGroupData = in.readParcelable(ShopGroupData.class.getClassLoader());
        allSelected = in.readByte() != 0;
        partialSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(shopGroupData, flags);
        dest.writeByte((byte) (allSelected ? 1 : 0));
        dest.writeByte((byte) (partialSelected ? 1 : 0));
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

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
        this.shopGroupData.setChecked(allSelected);
    }

    public boolean isPartialSelected() {
        return partialSelected;
    }

    public void setPartialSelected(boolean partialSelected) {
        this.partialSelected = partialSelected;
    }
}
