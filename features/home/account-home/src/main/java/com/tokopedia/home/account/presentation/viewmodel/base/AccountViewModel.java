package com.tokopedia.home.account.presentation.viewmodel.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountViewModel implements Parcelable {
    private BuyerViewModel buyerViewModel;
    private SellerViewModel sellerViewModel;
    private Boolean isSeller;

    public BuyerViewModel getBuyerViewModel() {
        return buyerViewModel;
    }

    public void setBuyerViewModel(BuyerViewModel buyerViewModel) {
        this.buyerViewModel = buyerViewModel;
    }

    public SellerViewModel getSellerViewModel() {
        return sellerViewModel;
    }

    public void setSellerViewModel(SellerViewModel sellerViewModel) {
        this.sellerViewModel = sellerViewModel;
    }

    public Boolean isSeller() {
        return isSeller;
    }

    public void setSeller(Boolean seller) {
        isSeller = seller;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.buyerViewModel, flags);
        dest.writeParcelable(this.sellerViewModel, flags);
        dest.writeValue(this.isSeller);
    }

    public AccountViewModel() {
    }

    protected AccountViewModel(Parcel in) {
        this.buyerViewModel = in.readParcelable(BuyerViewModel.class.getClassLoader());
        this.sellerViewModel = in.readParcelable(SellerViewModel.class.getClassLoader());
        this.isSeller = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<AccountViewModel> CREATOR = new Creator<AccountViewModel>() {
        @Override
        public AccountViewModel createFromParcel(Parcel source) {
            return new AccountViewModel(source);
        }

        @Override
        public AccountViewModel[] newArray(int size) {
            return new AccountViewModel[size];
        }
    };
}
