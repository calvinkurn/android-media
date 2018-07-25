package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String menu;
    private String menuDescription;
    private String applink;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(@NonNull String applink) {
        this.applink = applink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.menu);
        dest.writeString(this.menuDescription);
        dest.writeString(this.applink);
    }

    public MenuListViewModel() {
    }

    protected MenuListViewModel(Parcel in) {
        this.menu = in.readString();
        this.menuDescription = in.readString();
        this.applink = in.readString();
    }

    public static final Creator<MenuListViewModel> CREATOR = new Creator<MenuListViewModel>() {
        @Override
        public MenuListViewModel createFromParcel(Parcel source) {
            return new MenuListViewModel(source);
        }

        @Override
        public MenuListViewModel[] newArray(int size) {
            return new MenuListViewModel[size];
        }
    };
}
