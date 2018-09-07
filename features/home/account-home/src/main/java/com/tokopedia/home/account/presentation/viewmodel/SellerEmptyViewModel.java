package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author by alvinatin on 14/08/18.
 */

public class SellerEmptyViewModel implements ParcelableViewModel<AccountTypeFactory> {

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public SellerEmptyViewModel() {
    }

    protected SellerEmptyViewModel(Parcel in) {
    }

    public static final Creator<SellerEmptyViewModel> CREATOR = new Creator<SellerEmptyViewModel>
            () {
        @Override
        public SellerEmptyViewModel createFromParcel(Parcel source) {
            return new SellerEmptyViewModel(source);
        }

        @Override
        public SellerEmptyViewModel[] newArray(int size) {
            return new SellerEmptyViewModel[size];
        }
    };
}
