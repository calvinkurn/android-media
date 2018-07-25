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

    public List<ParcelableViewModel> getItems() {
        return items;
    }

    public void setItems(List<ParcelableViewModel> items) {
        this.items = items;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.items);
    }

    public SellerViewModel() {
    }

    protected SellerViewModel(Parcel in) {
        this.items = new ArrayList<>();
        in.readList(this.items, ParcelableViewModel.class.getClassLoader());
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
