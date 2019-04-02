package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/27/18.
 */
public class AddProductViewModel implements ParcelableViewModel<AccountTypeFactory> {

    public static final Creator<AddProductViewModel> CREATOR = new Creator<AddProductViewModel>() {
        @Override
        public AddProductViewModel createFromParcel(Parcel source) {
            return new AddProductViewModel(source);
        }

        @Override
        public AddProductViewModel[] newArray(int size) {
            return new AddProductViewModel[size];
        }
    };


    public AddProductViewModel() {
    }

    protected AddProductViewModel(Parcel in) {
    }

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
}
