package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountViewModel implements Parcelable {
    private List<Visitable> buyerViewModels = new ArrayList<>();
    private List<Visitable> sellerViewModels = new ArrayList<>();
    private Boolean isSeller;

    public List<Visitable> getBuyerViewModels() {
        return buyerViewModels;
    }

    public void setBuyerViewModels(@NonNull List<Visitable> buyerViewModels) {
        this.buyerViewModels = buyerViewModels;
    }

    public List<Visitable> getSellerViewModels() {
        return sellerViewModels;
    }

    public void setSellerViewModels(@NonNull List<Visitable> sellerViewModels) {
        this.sellerViewModels = sellerViewModels;
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
        dest.writeList(this.buyerViewModels);
        dest.writeList(this.sellerViewModels);
        dest.writeValue(this.isSeller);
    }

    public AccountViewModel() {
    }

    protected AccountViewModel(Parcel in) {
        this.buyerViewModels = new ArrayList<Visitable>();
        in.readList(this.buyerViewModels, Visitable.class.getClassLoader());
        this.sellerViewModels = new ArrayList<Visitable>();
        in.readList(this.sellerViewModels, Visitable.class.getClassLoader());
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
