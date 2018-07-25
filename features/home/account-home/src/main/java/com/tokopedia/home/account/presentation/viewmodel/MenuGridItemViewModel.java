package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridItemViewModel implements Parcelable {
    @DrawableRes
    private int resourceId;
    private String imageUrl;
    private String description;
    private String applink;
    private int count;

    public MenuGridItemViewModel(int resourceId, String description, String applink, int count) {
        this.resourceId = resourceId;
        this.description = description;
        this.applink = applink;
        this.count = count;
    }

    public MenuGridItemViewModel(String imageUrl, String description, String applink, int count) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.applink = applink;
        this.count = count;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resourceId);
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
        dest.writeString(this.applink);
        dest.writeInt(this.count);
    }

    protected MenuGridItemViewModel(Parcel in) {
        this.resourceId = in.readInt();
        this.imageUrl = in.readString();
        this.description = in.readString();
        this.applink = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<MenuGridItemViewModel> CREATOR = new Creator<MenuGridItemViewModel>() {
        @Override
        public MenuGridItemViewModel createFromParcel(Parcel source) {
            return new MenuGridItemViewModel(source);
        }

        @Override
        public MenuGridItemViewModel[] newArray(int size) {
            return new MenuGridItemViewModel[size];
        }
    };
}
