package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.DrawableRes;

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

    private String titleTrack; // for tracking
    private String sectionTrack;

    public MenuGridItemViewModel(int resourceId, String description, String applink, int count,
                                 String titleTrack, String sectionTrack) {
        this.resourceId = resourceId;
        this.description = description;
        this.applink = applink;
        this.count = count;
        this.titleTrack = titleTrack;
        this.sectionTrack = sectionTrack;
    }

    public MenuGridItemViewModel(String imageUrl, String description, String applink, int count,
                                 String titleTrack, String sectionTrack) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.applink = applink;
        this.count = count;
        this.titleTrack = titleTrack;
        this.sectionTrack = sectionTrack;
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
        dest.writeInt(this.resourceId);
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
        dest.writeString(this.applink);
        dest.writeInt(this.count);
        dest.writeString(this.titleTrack);
        dest.writeString(this.sectionTrack);
    }

    protected MenuGridItemViewModel(Parcel in) {
        this.resourceId = in.readInt();
        this.imageUrl = in.readString();
        this.description = in.readString();
        this.applink = in.readString();
        this.count = in.readInt();
        this.titleTrack = in.readString();
        this.sectionTrack = in.readString();
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
