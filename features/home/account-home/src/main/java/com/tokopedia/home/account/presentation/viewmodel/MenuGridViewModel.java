package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

import java.util.List;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String title;
    private String linkText;
    private String applinkUrl;
    private List<MenuGridItemViewModel> items;

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

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getApplinkUrl() {
        return applinkUrl;
    }

    public void setApplinkUrl(String applinkUrl) {
        this.applinkUrl = applinkUrl;
    }

    public List<MenuGridItemViewModel> getItems() {
        return items;
    }

    public void setItems(List<MenuGridItemViewModel> items) {
        this.items = items;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.linkText);
        dest.writeString(this.applinkUrl);
        dest.writeTypedList(this.items);
    }

    public MenuGridViewModel() {
    }

    protected MenuGridViewModel(Parcel in) {
        this.title = in.readString();
        this.linkText = in.readString();
        this.applinkUrl = in.readString();
        this.items = in.createTypedArrayList(MenuGridItemViewModel.CREATOR);
    }

    public static final Creator<MenuGridViewModel> CREATOR = new Creator<MenuGridViewModel>() {
        @Override
        public MenuGridViewModel createFromParcel(Parcel source) {
            return new MenuGridViewModel(source);
        }

        @Override
        public MenuGridViewModel[] newArray(int size) {
            return new MenuGridViewModel[size];
        }
    };
}
