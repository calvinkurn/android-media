package com.tokopedia.home.account.presentation.viewmodel.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/25/18.
 */
public class SellerViewModel implements Parcelable {
    private List<ParcelableViewModel> items = new ArrayList<>();
    private Boolean isSeller;
    private boolean hasSaldoBalance = false;

    public List<ParcelableViewModel> getItems() {
        return items;
    }

    public void setItems(List<ParcelableViewModel> items) {
        this.items = items;
    }

    public Boolean getSeller() {
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
        dest.writeTypedList(this.items);
        dest.writeValue(this.isSeller);
    }

    public SellerViewModel() {
    }

    public boolean hasSaldoBalance() {
        return hasSaldoBalance;
    }

    public void setHasSaldoBalance(boolean hasSaldoBalance) {
        this.hasSaldoBalance = hasSaldoBalance;
    }

    protected SellerViewModel(Parcel in) {
        this.items = new ArrayList<>();
        in.readList(this.items, ParcelableViewModel.class.getClassLoader());
        this.isSeller = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<SellerViewModel> CREATOR = new Creator<SellerViewModel>() {
        @Override
        public SellerViewModel createFromParcel(Parcel source) {
            return new SellerViewModel(source);
        }

        @Override
        public SellerViewModel[] newArray(int size) {
            return new SellerViewModel[size];
        }
    };
}
