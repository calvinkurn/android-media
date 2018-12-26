package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 15/02/18.
 */

public class CartListData implements Parcelable {
    private boolean isError;
    private String errorMessage;

    private List<ShopGroupData> shopGroupDataList = new ArrayList<>();
    private CartPromoSuggestion cartPromoSuggestion;
    private boolean promoCouponActive;
    private CartTickerErrorData cartTickerErrorData;
    private AutoApplyData autoApplyData;
    private String defaultPromoDialogTab;
    private boolean allSelected;
    private TopAdsModel adsModel;

    public TopAdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(TopAdsModel adsModel) {
        this.adsModel = adsModel;
    }

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

    public CartPromoSuggestion getCartPromoSuggestion() {
        return cartPromoSuggestion;
    }

    public void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestion = cartPromoSuggestion;
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

    public AutoApplyData getAutoApplyData() {
        return autoApplyData;
    }

    public void setAutoApplyData(AutoApplyData autoApplyData) {
        this.autoApplyData = autoApplyData;
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
        dest.writeTypedList(this.shopGroupDataList);
        dest.writeParcelable(this.cartPromoSuggestion, flags);
        dest.writeByte(this.promoCouponActive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allSelected ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.cartTickerErrorData, flags);
        dest.writeParcelable(this.autoApplyData, flags);
    }

    protected CartListData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.shopGroupDataList = in.createTypedArrayList(ShopGroupData.CREATOR);
        this.cartPromoSuggestion = in.readParcelable(CartPromoSuggestion.class.getClassLoader());
        this.promoCouponActive = in.readByte() != 0;
        this.allSelected = in.readByte() != 0;
        this.cartTickerErrorData = in.readParcelable(CartTickerErrorData.class.getClassLoader());
        this.autoApplyData = in.readParcelable(AutoApplyData.class.getClassLoader());
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
