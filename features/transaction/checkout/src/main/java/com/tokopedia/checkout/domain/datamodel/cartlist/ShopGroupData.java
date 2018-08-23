package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartItemHolderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class ShopGroupData implements Parcelable {

    private List<CartItemHolderData> cartItemHolderDataList = new ArrayList<>();
    private boolean isError;
    private String errorMessage;
    private String shopName;
    private String shopId;
    private String shopType;
    private boolean isGoldMerchant;
    private boolean isOfficialStore;

    // Total data which is calculated from cartItemDataList
    private long totalPrice;
    private long totalCashback;
    private int totalItem;

    public ShopGroupData() {
    }

    protected ShopGroupData(Parcel in) {
        cartItemHolderDataList = in.createTypedArrayList(CartItemHolderData.CREATOR);
        isError = in.readByte() != 0;
        errorMessage = in.readString();
        shopName = in.readString();
        shopId = in.readString();
        shopType = in.readString();
        isGoldMerchant = in.readByte() != 0;
        isOfficialStore = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cartItemHolderDataList);
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeString(errorMessage);
        dest.writeString(shopName);
        dest.writeString(shopId);
        dest.writeString(shopType);
        dest.writeByte((byte) (isGoldMerchant ? 1 : 0));
        dest.writeByte((byte) (isOfficialStore ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopGroupData> CREATOR = new Creator<ShopGroupData>() {
        @Override
        public ShopGroupData createFromParcel(Parcel in) {
            return new ShopGroupData(in);
        }

        @Override
        public ShopGroupData[] newArray(int size) {
            return new ShopGroupData[size];
        }
    };

    public List<CartItemHolderData> getCartItemDataList() {
        return cartItemHolderDataList;
    }

    public void setCartItemDataList(List<CartItemData> cartItemDataList) {
        for (CartItemData cartItemData : cartItemDataList) {
            CartItemHolderData cartItemHolderData = new CartItemHolderData();
            cartItemHolderData.setCartItemData(cartItemData);
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderData.setErrorFormItemValidationMessage("");
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderData.setSelected(true);
            cartItemHolderDataList.add(cartItemHolderData);
        }
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean officialStore) {
        isOfficialStore = officialStore;
    }
}
