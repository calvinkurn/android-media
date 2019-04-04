package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String userId;
    private String name;
    private String imageUrl;
    private String tokopoint;
    private int coupons;
    private int progress;
    private boolean isAffiliate;

    public BuyerCardViewModel() {
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTokopoint() {
        return tokopoint;
    }

    public void setTokopoint(String tokopoint) {
        this.tokopoint = tokopoint;
    }

    public int getCoupons() {
        return coupons;
    }

    public void setCoupons(int coupons) {
        this.coupons = coupons;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isAffiliate() {
        return isAffiliate;
    }

    public void setAffiliate(boolean affiliate) {
        isAffiliate = affiliate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.tokopoint);
        dest.writeInt(this.coupons);
        dest.writeInt(this.progress);
        dest.writeByte(this.isAffiliate ? (byte) 1 : (byte) 0);
    }

    protected BuyerCardViewModel(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.tokopoint = in.readString();
        this.coupons = in.readInt();
        this.progress = in.readInt();
        this.isAffiliate = in.readByte() != 0;
    }

    public static final Creator<BuyerCardViewModel> CREATOR = new Creator<BuyerCardViewModel>() {
        @Override
        public BuyerCardViewModel createFromParcel(Parcel source) {
            return new BuyerCardViewModel(source);
        }

        @Override
        public BuyerCardViewModel[] newArray(int size) {
            return new BuyerCardViewModel[size];
        }
    };
}
