package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

public class ShopGroupData implements Parcelable {

    private List<CartItemHolderData> cartItemHolderDataList = new ArrayList<>();
    private boolean isChecked;
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
    private boolean hasPromoList;
    private String cartString;
    private VoucherOrdersItemData voucherOrdersItemData;

    // Total data which is calculated from cartItemDataList
    private long totalPrice;
    private long totalCashback;
    private int totalItem;

    public ShopGroupData() {
    }

    protected ShopGroupData(Parcel in) {
        cartItemHolderDataList = in.createTypedArrayList(CartItemHolderData.CREATOR);
        isChecked = in.readByte() != 0;
        isError = in.readByte() != 0;
        errorTitle = in.readString();
        errorDescription = in.readString();
        isWarning = in.readByte() != 0;
        warningTitle = in.readString();
        warningDescription = in.readString();
        shopName = in.readString();
        shopId = in.readString();
        shopType = in.readString();
        isGoldMerchant = in.readByte() != 0;
        isOfficialStore = in.readByte() != 0;
        shopBadge = in.readString();
        isFulfillment = in.readByte() != 0;
        fulfillmentName = in.readString();
        hasPromoList = in.readByte() != 0;
        cartString = in.readString();
        voucherOrdersItemData = in.readParcelable(VoucherOrdersItemData.class.getClassLoader());
        totalPrice = in.readLong();
        totalCashback = in.readLong();
        totalItem = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cartItemHolderDataList);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeString(errorTitle);
        dest.writeString(errorDescription);
        dest.writeByte((byte) (isWarning ? 1 : 0));
        dest.writeString(warningTitle);
        dest.writeString(warningDescription);
        dest.writeString(shopName);
        dest.writeString(shopId);
        dest.writeString(shopType);
        dest.writeByte((byte) (isGoldMerchant ? 1 : 0));
        dest.writeByte((byte) (isOfficialStore ? 1 : 0));
        dest.writeString(shopBadge);
        dest.writeByte((byte) (isFulfillment ? 1 : 0));
        dest.writeString(fulfillmentName);
        dest.writeByte((byte) (hasPromoList ? 1 : 0));
        dest.writeString(cartString);
        dest.writeParcelable(voucherOrdersItemData, flags);
        dest.writeLong(totalPrice);
        dest.writeLong(totalCashback);
        dest.writeInt(totalItem);
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
            if (cartItemData.isError()) {
                cartItemHolderData.setSelected(false);
            } else {
                cartItemHolderData.setSelected(cartItemData.getOriginData().isCheckboxState());
            }
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

    public boolean isHasPromoList() {
        return hasPromoList;
    }

    public void setHasPromoList(boolean hasPromoList) {
        this.hasPromoList = hasPromoList;
    }

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }

    public VoucherOrdersItemData getVoucherOrdersItemData() {
        return voucherOrdersItemData;
    }

    public void setVoucherOrdersItemData(VoucherOrdersItemData voucherOrdersItemData) {
        this.voucherOrdersItemData = voucherOrdersItemData;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
