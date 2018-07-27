package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/26/18.
 */
public class ShopCardViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String shopId;
    private String shopName;
    private String balance;
    private String shopImageUrl;
    private Boolean isGoldMerchant;
    private int medalType;
    private int level;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public Boolean getGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(Boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public int getMedalType() {
        return medalType;
    }

    public void setMedalType(int medalType) {
        this.medalType = medalType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ShopCardViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopName);
        dest.writeString(this.balance);
        dest.writeString(this.shopImageUrl);
        dest.writeValue(this.isGoldMerchant);
        dest.writeInt(this.medalType);
        dest.writeInt(this.level);
    }

    protected ShopCardViewModel(Parcel in) {
        this.shopName = in.readString();
        this.balance = in.readString();
        this.shopImageUrl = in.readString();
        this.isGoldMerchant = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.medalType = in.readInt();
        this.level = in.readInt();
    }

    public static final Creator<ShopCardViewModel> CREATOR = new Creator<ShopCardViewModel>() {
        @Override
        public ShopCardViewModel createFromParcel(Parcel source) {
            return new ShopCardViewModel(source);
        }

        @Override
        public ShopCardViewModel[] newArray(int size) {
            return new ShopCardViewModel[size];
        }
    };
}
