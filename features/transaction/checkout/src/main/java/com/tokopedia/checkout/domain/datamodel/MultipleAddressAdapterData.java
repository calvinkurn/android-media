package com.tokopedia.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapterData implements Parcelable {

    private String senderName;
    private String productImageUrl;
    private String productName;
    private String productPrice;
    private boolean isPreOrder;
    private boolean isFreeReturn;
    private boolean isCashBack;
    private boolean isOfficialStore;
    private boolean isGoldMerchant;
    private String officialStoreLogoUrl;
    private String goldMerchantLogoUrl;
    private String cashBackInfo;
    private List<MultipleAddressItemData> itemListData;

    public MultipleAddressAdapterData() {
    }

    protected MultipleAddressAdapterData(Parcel in) {
        senderName = in.readString();
        productImageUrl = in.readString();
        productName = in.readString();
        productPrice = in.readString();
        isPreOrder = in.readByte() != 0;
        isFreeReturn = in.readByte() != 0;
        isCashBack = in.readByte() != 0;
        isOfficialStore = in.readByte() != 0;
        isGoldMerchant = in.readByte() != 0;
        cashBackInfo = in.readString();
        officialStoreLogoUrl = in.readString();
        goldMerchantLogoUrl = in.readString();
        itemListData = in.createTypedArrayList(MultipleAddressItemData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderName);
        dest.writeString(productImageUrl);
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeByte((byte) (isPreOrder ? 1 : 0));
        dest.writeByte((byte) (isFreeReturn ? 1 : 0));
        dest.writeByte((byte) (isCashBack ? 1 : 0));
        dest.writeByte((byte) (isGoldMerchant ? 1 : 0));
        dest.writeByte((byte) (isOfficialStore ? 1 : 0));
        dest.writeString(cashBackInfo);
        dest.writeString(officialStoreLogoUrl);
        dest.writeString(goldMerchantLogoUrl);
        dest.writeTypedList(itemListData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MultipleAddressAdapterData> CREATOR = new Creator<MultipleAddressAdapterData>() {
        @Override
        public MultipleAddressAdapterData createFromParcel(Parcel in) {
            return new MultipleAddressAdapterData(in);
        }

        @Override
        public MultipleAddressAdapterData[] newArray(int size) {
            return new MultipleAddressAdapterData[size];
        }
    };

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public List<MultipleAddressItemData> getItemListData() {
        return itemListData;
    }

    public void setItemListData(List<MultipleAddressItemData> itemListData) {
        this.itemListData = itemListData;
    }

    public boolean isPreOrder() {
        return isPreOrder;
    }

    public void setPreOrder(boolean preOrder) {
        isPreOrder = preOrder;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        isFreeReturn = freeReturn;
    }

    public boolean isCashBack() {
        return isCashBack;
    }

    public void setCashBack(boolean cashBack) {
        isCashBack = cashBack;
    }

    public String getCashBackInfo() {
        return cashBackInfo;
    }

    public void setCashBackInfo(String cashBackInfo) {
        this.cashBackInfo = cashBackInfo;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean officialStore) {
        isOfficialStore = officialStore;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getOfficialStoreLogoUrl() {
        return officialStoreLogoUrl;
    }

    public void setOfficialStoreLogoUrl(String officialStoreLogoUrl) {
        this.officialStoreLogoUrl = officialStoreLogoUrl;
    }

    public String getGoldMerchantLogoUrl() {
        return goldMerchantLogoUrl;
    }

    public void setGoldMerchantLogoUrl(String goldMerchantLogoUrl) {
        this.goldMerchantLogoUrl = goldMerchantLogoUrl;
    }
}
