package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuTitleViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String title;

    public MenuTitleViewModel() {
    }

    public MenuTitleViewModel(String title) {
        this.title = title;
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
    }

    protected MenuTitleViewModel(Parcel in) {
        this.title = in.readString();
    }

    public static final Creator<MenuTitleViewModel> CREATOR = new Creator<MenuTitleViewModel>() {
        @Override
        public MenuTitleViewModel createFromParcel(Parcel source) {
            return new MenuTitleViewModel(source);
        }

        @Override
        public MenuTitleViewModel[] newArray(int size) {
            return new MenuTitleViewModel[size];
        }
    };
}
