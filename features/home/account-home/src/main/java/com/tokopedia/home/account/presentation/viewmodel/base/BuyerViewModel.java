package com.tokopedia.home.account.presentation.viewmodel.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/25/18.
 */
public class BuyerViewModel implements Parcelable {
    private List<ParcelableViewModel> items = new ArrayList<>();
    private boolean onBoardingForBuyerSaldo = false;

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
        dest.writeByte((byte) (onBoardingForBuyerSaldo ? 1 : 0));
    }

    public BuyerViewModel() {
    }

    protected BuyerViewModel(Parcel in) {
        this.items = new ArrayList<>();
        in.readList(this.items, ParcelableViewModel.class.getClassLoader());
        onBoardingForBuyerSaldo = in.readByte() != 0;
    }

    public boolean isOnBoardingForBuyerSaldo() {
        return onBoardingForBuyerSaldo;
    }

    public void setOnBoardingForBuyerSaldo(boolean onBoardingForBuyerSaldo) {
        this.onBoardingForBuyerSaldo = onBoardingForBuyerSaldo;
    }

    public static final Creator<BuyerViewModel> CREATOR = new Creator<BuyerViewModel>() {
        @Override
        public BuyerViewModel createFromParcel(Parcel source) {
            return new BuyerViewModel(source);
        }

        @Override
        public BuyerViewModel[] newArray(int size) {
            return new BuyerViewModel[size];
        }
    };
}
