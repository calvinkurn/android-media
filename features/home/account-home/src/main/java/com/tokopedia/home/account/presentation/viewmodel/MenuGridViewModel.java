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

    private String titleTrack; // for tracking
    private String sectionTrack;

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

    public String getTitleTrack() {
        return titleTrack;
    }

    public void setTitleTrack(String titleTrack) {
        this.titleTrack = titleTrack;
    }

    public String getSectionTrack() {
        return sectionTrack;
    }

    public void setSectionTrack(String sectionTrack) {
        this.sectionTrack = sectionTrack;
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
        dest.writeString(this.titleTrack);
        dest.writeString(this.sectionTrack);
    }

    public MenuGridViewModel() {
    }

    protected MenuGridViewModel(Parcel in) {
        this.title = in.readString();
        this.linkText = in.readString();
        this.applinkUrl = in.readString();
        this.items = in.createTypedArrayList(MenuGridItemViewModel.CREATOR);
        this.titleTrack = in.readString();
        this.sectionTrack = in.readString();
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
