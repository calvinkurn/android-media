package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/24/18.
 */
public class InfoCardViewModel implements ParcelableViewModel<AccountTypeFactory> {
    @DrawableRes
    private int iconRes;
    private String iconUrl;
    private String mainText;
    private String secondaryText;
    private String applink;

    private String titleTrack;
    private String sectionTrack;
    private String itemTrack;

    private int newTxtVisiblle = View.GONE;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
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

    public String getItemTrack() {
        return itemTrack;
    }

    public void setItemTrack(String itemTrack) {
        this.itemTrack = itemTrack;
    }

    public int isNewTxtVisiblle() {
        return newTxtVisiblle;
    }

    public void setNewTxtVisiblle(int newTxtVisiblle) {
        this.newTxtVisiblle = newTxtVisiblle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iconRes);
        dest.writeString(this.iconUrl);
        dest.writeString(this.mainText);
        dest.writeString(this.secondaryText);
        dest.writeString(this.applink);
        dest.writeString(this.titleTrack);
        dest.writeString(this.sectionTrack);
        dest.writeInt(this.newTxtVisiblle);
    }

    public InfoCardViewModel() {
    }

    protected InfoCardViewModel(Parcel in) {
        this.iconRes = in.readInt();
        this.iconUrl = in.readString();
        this.mainText = in.readString();
        this.secondaryText = in.readString();
        this.applink = in.readString();
        this.titleTrack = in.readString();
        this.sectionTrack = in.readString();
        this.newTxtVisiblle = in.readInt();
    }

    public static final Creator<InfoCardViewModel> CREATOR = new Creator<InfoCardViewModel>() {
        @Override
        public InfoCardViewModel createFromParcel(Parcel source) {
            return new InfoCardViewModel(source);
        }

        @Override
        public InfoCardViewModel[] newArray(int size) {
            return new InfoCardViewModel[size];
        }
    };
}
