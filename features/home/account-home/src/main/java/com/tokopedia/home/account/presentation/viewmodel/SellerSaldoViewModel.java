package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

public class SellerSaldoViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String depositFmt;

    public void setBalance(String depositFmt) {
        this.depositFmt = depositFmt;
    }

    public String getDepositFmt() {
        return depositFmt;
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public SellerSaldoViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.depositFmt);

    }

    protected SellerSaldoViewModel(Parcel in) {
        this.depositFmt = in.readString();
    }

    public static final Creator<SellerSaldoViewModel> CREATOR = new Creator<SellerSaldoViewModel>() {
        @Override
        public SellerSaldoViewModel createFromParcel(Parcel source) {
            return new SellerSaldoViewModel(source);
        }

        @Override
        public SellerSaldoViewModel[] newArray(int size) {
            return new SellerSaldoViewModel[size];
        }
    };
}
