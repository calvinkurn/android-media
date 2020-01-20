package com.tokopedia.purchase_platform.features.cart.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class CartShopHolderData implements Parcelable {

    private ShopGroupAvailableData shopGroupAvailableData;
    private boolean allSelected;
    private boolean partialSelected;

    public CartShopHolderData() {
    }

    protected CartShopHolderData(Parcel in) {
        shopGroupAvailableData = in.readParcelable(ShopGroupAvailableData.class.getClassLoader());
        allSelected = in.readByte() != 0;
        partialSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(shopGroupAvailableData, flags);
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

    public ShopGroupAvailableData getShopGroupAvailableData() {
        return shopGroupAvailableData;
    }

    public void setShopGroupAvailableData(ShopGroupAvailableData shopGroupAvailableData) {
        this.shopGroupAvailableData = shopGroupAvailableData;
    }

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
        this.shopGroupAvailableData.setChecked(allSelected);
    }

    public boolean isPartialSelected() {
        return partialSelected;
    }

    public void setPartialSelected(boolean partialSelected) {
        this.partialSelected = partialSelected;
    }
}
