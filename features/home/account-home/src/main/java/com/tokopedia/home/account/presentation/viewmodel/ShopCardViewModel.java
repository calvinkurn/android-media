package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.topads.common.data.model.DataDeposit;

/**
 * @author okasurya on 7/26/18.
 */
public class ShopCardViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String shopId;
    private String shopName;
    private String shopImageUrl;
    private Boolean isGoldMerchant;
    private String reputationImageUrl;
    private String verificationStatusName;
    private int verificationStatus;
    private DataDeposit dataDeposit;

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

    public String getReputationImageUrl() {
        return reputationImageUrl;
    }

    public void setReputationImageUrl(String reputationImageUrl) {
        this.reputationImageUrl = reputationImageUrl;
    }

    public String getVerificationStatusName() {
        return verificationStatusName;
    }

    public void setVerificationStatusName(String verificationStatusName) {
        this.verificationStatusName = verificationStatusName;
    }

    public int getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(int verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public DataDeposit getDataDeposit() {
        return dataDeposit;
    }

    public void setDataDeposit(DataDeposit dataDeposit) {
        this.dataDeposit = dataDeposit;
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
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopImageUrl);
        dest.writeValue(this.isGoldMerchant);
        dest.writeString(this.reputationImageUrl);
        dest.writeString(this.verificationStatusName);
        dest.writeInt(this.verificationStatus);
        dest.writeParcelable(this.dataDeposit, flags);

    }

    protected ShopCardViewModel(Parcel in) {
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.shopImageUrl = in.readString();
        this.isGoldMerchant = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.reputationImageUrl = in.readString();
        this.verificationStatusName = in.readString();
        this.verificationStatus = in.readInt();
        this.dataDeposit = in.readParcelable(DataDeposit.class.getClassLoader());
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
