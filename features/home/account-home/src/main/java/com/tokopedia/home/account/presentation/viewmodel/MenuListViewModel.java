package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import androidx.annotation.NonNull;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String menu;
    private String menuDescription;
    private String applink;
    private int count;
    private String titleTrack;
    private String sectionTrack;
    private boolean isUseSeparator = true;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public MenuListViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isUseSeparator() {
        return isUseSeparator;
    }

    public void setUseSeparator(boolean useSeparator) {
        this.isUseSeparator = useSeparator;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.menu);
        dest.writeString(this.menuDescription);
        dest.writeString(this.applink);
        dest.writeInt(this.count);
        dest.writeString(this.titleTrack);
        dest.writeString(this.sectionTrack);
        dest.writeByte((byte) (isUseSeparator ? 1 : 0));
    }

    protected MenuListViewModel(Parcel in) {
        this.menu = in.readString();
        this.menuDescription = in.readString();
        this.applink = in.readString();
        this.count = in.readInt();
        this.titleTrack = in.readString();
        this.sectionTrack = in.readString();
        this.isUseSeparator = in.readByte() != 0;
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
