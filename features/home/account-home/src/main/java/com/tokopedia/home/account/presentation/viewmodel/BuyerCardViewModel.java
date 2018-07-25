package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewModel implements ParcelableViewModel<AccountTypeFactory> {

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
    private String name;
    private String imageUrl;
    private String tokopoint;
    private String coupons;
    private int progress;

    public BuyerCardViewModel() {
    }

    protected BuyerCardViewModel(Parcel in) {
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.tokopoint = in.readString();
        this.coupons = in.readString();
        this.progress = in.readInt();
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
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

    public String getVoucher() {
        return coupons;
    }

    public void setVoucher(String coupons) {
        this.coupons = coupons;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.tokopoint);
        dest.writeString(this.coupons);
        dest.writeInt(this.progress);
    }
}
