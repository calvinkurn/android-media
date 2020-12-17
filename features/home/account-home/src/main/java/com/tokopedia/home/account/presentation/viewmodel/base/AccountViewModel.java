package com.tokopedia.home.account.presentation.viewmodel.base;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountViewModel implements Parcelable {
    private BuyerViewModel buyerViewModel;
    private Boolean isSeller;

    public BuyerViewModel getBuyerViewModel() {
        return buyerViewModel;
    }

    public void setBuyerViewModel(BuyerViewModel buyerViewModel) {
        this.buyerViewModel = buyerViewModel;
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
        dest.writeValue(this.isSeller);
    }

    public AccountViewModel() {
    }

    protected AccountViewModel(Parcel in) {
        this.buyerViewModel = in.readParcelable(BuyerViewModel.class.getClassLoader());
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
