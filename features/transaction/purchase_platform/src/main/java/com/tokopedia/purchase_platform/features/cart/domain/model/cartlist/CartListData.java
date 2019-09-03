package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartTickerData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData;
import com.tokopedia.purchase_platform.common.feature.promo_global.data.model.response.GlobalCouponAttr;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 15/02/18.
 */

public class CartListData implements Parcelable {
    private boolean isError;
    private String errorMessage;
    private CartTickerData ticker;

    private List<ShopGroupData> shopGroupDataList = new ArrayList<>();
    private CartPromoSuggestionHolderData cartPromoSuggestionHolderData;
    private boolean promoCouponActive;
    private CartTickerErrorData cartTickerErrorData;
    private AutoApplyData autoApplyData;
    private AutoApplyStackData autoApplyStackData;
    private GlobalCouponAttr globalCouponAttr;
    private String defaultPromoDialogTab;
    private boolean allSelected;
    private boolean isShowOnboarding;

    public String getDefaultPromoDialogTab() {
        return defaultPromoDialogTab;
    }

    public void setDefaultPromoDialogTab(String defaultPromoDialogTab) {
        this.defaultPromoDialogTab = defaultPromoDialogTab;
    }

    public CartTickerErrorData getCartTickerErrorData() {
        return cartTickerErrorData;
    }

    public void setCartTickerErrorData(CartTickerErrorData cartTickerErrorData) {
        this.cartTickerErrorData = cartTickerErrorData;
    }

    public boolean isPromoCouponActive() {
        return promoCouponActive;
    }

    public void setPromoCouponActive(boolean promoCouponActive) {
        this.promoCouponActive = promoCouponActive;
    }

    public CartPromoSuggestionHolderData getCartPromoSuggestionHolderData() {
        return cartPromoSuggestionHolderData;
    }

    public void setCartPromoSuggestionHolderData(CartPromoSuggestionHolderData cartPromoSuggestionHolderData) {
        this.cartPromoSuggestionHolderData = cartPromoSuggestionHolderData;
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

    public CartTickerData getTicker() {
        return ticker;
    }

    public void setTicker(CartTickerData ticker) {
        this.ticker = ticker;
    }

    public AutoApplyData getAutoApplyData() {
        return autoApplyData;
    }

    public void setAutoApplyData(AutoApplyData autoApplyData) {
        this.autoApplyData = autoApplyData;
    }

    public AutoApplyStackData getAutoApplyStackData() {
        return autoApplyStackData;
    }

    public void setAutoApplyStackData(AutoApplyStackData autoApplyStackData) {
        this.autoApplyStackData = autoApplyStackData;
    }

    public GlobalCouponAttr getGlobalCouponAttr() {
        return globalCouponAttr;
    }

    public void setGlobalCouponAttr(GlobalCouponAttr globalCouponAttr) {
        this.globalCouponAttr = globalCouponAttr;
    }

    public List<ShopGroupData> getShopGroupDataList() {
        return shopGroupDataList;
    }

    public void setShopGroupDataList(List<ShopGroupData> shopGroupDataList) {
        this.shopGroupDataList = shopGroupDataList;
    }

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
    }

    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }

    public void setShowOnboarding(boolean showOnboarding) {
        isShowOnboarding = showOnboarding;
    }

    public CartListData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeParcelable(this.ticker, flags);
        dest.writeTypedList(this.shopGroupDataList);
        dest.writeParcelable(this.cartPromoSuggestionHolderData, flags);
        dest.writeByte(this.promoCouponActive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allSelected ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.cartTickerErrorData, flags);
        dest.writeParcelable(this.autoApplyData, flags);
        dest.writeByte(this.isShowOnboarding ? (byte) 1 : (byte) 0);
    }

    protected CartListData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.ticker = in.readParcelable(CartTickerData.class.getClassLoader());
        this.shopGroupDataList = in.createTypedArrayList(ShopGroupData.CREATOR);
        this.cartPromoSuggestionHolderData = in.readParcelable(CartPromoSuggestionHolderData.class.getClassLoader());
        this.promoCouponActive = in.readByte() != 0;
        this.allSelected = in.readByte() != 0;
        this.cartTickerErrorData = in.readParcelable(CartTickerErrorData.class.getClassLoader());
        this.autoApplyData = in.readParcelable(AutoApplyData.class.getClassLoader());
        this.isShowOnboarding = in.readByte() != 0;
    }

    public static final Creator<CartListData> CREATOR = new Creator<CartListData>() {
        @Override
        public CartListData createFromParcel(Parcel source) {
            return new CartListData(source);
        }

        @Override
        public CartListData[] newArray(int size) {
            return new CartListData[size];
        }
    };
}
