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
    private String errorTitle;
    private String errorDescription;
    private boolean isWarning;
    private String warningTitle;
    private String warningDescription;
    private String shopName;
    private String shopId;
    private String shopType;
    private boolean isGoldMerchant;
    private boolean isOfficialStore;
    private String shopBadge;
    private boolean isFulfillment;
    private String fulfillmentName;

    // Total data which is calculated from cartItemDataList
    private long totalPrice;
    private long totalCashback;
    private int totalItem;

    public ShopGroupData() {
    }

    public List<CartItemHolderData> getCartItemDataList() {
        return cartItemHolderDataList;
    }

    public void setCartItemDataList(List<CartItemData> cartItemDataList, boolean isError) {
        for (CartItemData cartItemData : cartItemDataList) {
            CartItemHolderData cartItemHolderData = new CartItemHolderData();
            cartItemHolderData.setCartItemData(cartItemData);
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderData.setErrorFormItemValidationMessage("");
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderData.setSelected(!isError);
            cartItemHolderDataList.add(cartItemHolderData);
        }
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
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

    public List<CartItemHolderData> getCartItemHolderDataList() {
        return cartItemHolderDataList;
    }

    public void setCartItemHolderDataList(List<CartItemHolderData> cartItemHolderDataList) {
        this.cartItemHolderDataList = cartItemHolderDataList;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningTitle() {
        return warningTitle;
    }

    public void setWarningTitle(String warningTitle) {
        this.warningTitle = warningTitle;
    }

    public String getWarningDescription() {
        return warningDescription;
    }

    public void setWarningDescription(String warningDescription) {
        this.warningDescription = warningDescription;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTotalCashback() {
        return totalCashback;
    }

    public void setTotalCashback(long totalCashback) {
        this.totalCashback = totalCashback;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public String getShopBadge() {
        return shopBadge;
    }

    public void setShopBadge(String shopBadge) {
        this.shopBadge = shopBadge;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public void setFulfillment(boolean fulfillment) {
        isFulfillment = fulfillment;
    }

    public String getFulfillmentName() {
        return fulfillmentName;
    }

    public void setFulfillmentName(String fulfillmentName) {
        this.fulfillmentName = fulfillmentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.cartItemHolderDataList);
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorTitle);
        dest.writeString(this.errorDescription);
        dest.writeByte(this.isWarning ? (byte) 1 : (byte) 0);
        dest.writeString(this.warningTitle);
        dest.writeString(this.warningDescription);
        dest.writeString(this.shopName);
        dest.writeString(this.shopId);
        dest.writeString(this.shopType);
        dest.writeByte(this.isGoldMerchant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOfficialStore ? (byte) 1 : (byte) 0);
        dest.writeString(this.shopBadge);
        dest.writeByte(this.isFulfillment ? (byte) 1 : (byte) 0);
        dest.writeString(this.fulfillmentName);
        dest.writeLong(this.totalPrice);
        dest.writeLong(this.totalCashback);
        dest.writeInt(this.totalItem);
    }

    protected ShopGroupData(Parcel in) {
        this.cartItemHolderDataList = in.createTypedArrayList(CartItemHolderData.CREATOR);
        this.isError = in.readByte() != 0;
        this.errorTitle = in.readString();
        this.errorDescription = in.readString();
        this.isWarning = in.readByte() != 0;
        this.warningTitle = in.readString();
        this.warningDescription = in.readString();
        this.shopName = in.readString();
        this.shopId = in.readString();
        this.shopType = in.readString();
        this.isGoldMerchant = in.readByte() != 0;
        this.isOfficialStore = in.readByte() != 0;
        this.shopBadge = in.readString();
        this.isFulfillment = in.readByte() != 0;
        this.fulfillmentName = in.readString();
        this.totalPrice = in.readLong();
        this.totalCashback = in.readLong();
        this.totalItem = in.readInt();
    }

    public static final Creator<ShopGroupData> CREATOR = new Creator<ShopGroupData>() {
        @Override
        public ShopGroupData createFromParcel(Parcel source) {
            return new ShopGroupData(source);
        }

        @Override
        public ShopGroupData[] newArray(int size) {
            return new ShopGroupData[size];
        }
    };
}
