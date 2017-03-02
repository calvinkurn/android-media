package com.tokopedia.core.shop.model;

import com.tokopedia.core.shop.model.shopData.Data;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 5/23/2016.
 */
@Parcel
public class ShopEditorModel {
    String mShopAvaUri = "";
    boolean UploadingAvatar = false;
    String mShopName= "";
    String mShopSlogan= "";
    String mShopDesc= "";
    String IsAllowShop = "0";
    String mShopNameText= "";
    String mShopSloganText= "";
    String mShopDescText= "";
    Data modelShopData;

    public String getmShopNameText() {
        return mShopNameText;
    }

    public void setmShopNameText(String mShopNameText) {
        this.mShopNameText = mShopNameText;
    }

    public String getmShopSloganText() {
        return mShopSloganText;
    }

    public void setmShopSloganText(String mShopSloganText) {
        this.mShopSloganText = mShopSloganText;
    }

    public String getmShopDescText() {
        return mShopDescText;
    }

    public void setmShopDescText(String mShopDescText) {
        this.mShopDescText = mShopDescText;
    }

    public String getmShopAvaUri() {
        return mShopAvaUri;
    }

    public void setmShopAvaUri(String mShopAvaUri) {
        this.mShopAvaUri = mShopAvaUri;
    }

    public boolean isUploadingAvatar() {
        return UploadingAvatar;
    }

    public void setUploadingAvatar(boolean uploadingAvatar) {
        UploadingAvatar = uploadingAvatar;
    }

    public String getmShopName() {
        return mShopName;
    }

    public void setmShopName(String mShopName) {
        this.mShopName = mShopName;
    }

    public String getmShopSlogan() {
        return mShopSlogan;
    }

    public void setmShopSlogan(String mShopSlogan) {
        this.mShopSlogan = mShopSlogan;
    }

    public String getmShopDesc() {
        return mShopDesc;
    }

    public void setmShopDesc(String mShopDesc) {
        this.mShopDesc = mShopDesc;
    }

    public String getIsAllowShop() {
        return IsAllowShop;
    }

    public void setIsAllowShop(String isAllowShop) {
        IsAllowShop = isAllowShop;
    }

    public Data getModelShopData() {
        return modelShopData;
    }

    public void setModelShopData(Data modelShopData) {
        this.modelShopData = modelShopData;
    }
}
