package com.tokopedia.affiliate.feature.dashboard.domain.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardItemDomain implements Parcelable {
    private String imageUrl;
    private String title;
    private String value;
    private String itemClicked;
    private String itemSold;
    private boolean isActive;

    public DashboardItemDomain(String imageUrl,
                                  String title,
                                  String value,
                                  String itemClicked,
                                  String itemSold,
                                  boolean isActive) {
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.title = title;
        this.value = value;
        this.itemClicked = itemClicked;
        this.itemSold = itemSold;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getItemClicked() {
        return itemClicked;
    }

    public void setItemClicked(String itemClicked) {
        this.itemClicked = itemClicked;
    }

    public String getItemSold() {
        return itemSold;
    }

    public void setItemSold(String itemSold) {
        this.itemSold = itemSold;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.value);
        dest.writeString(this.itemClicked);
        dest.writeString(this.itemSold);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
    }

    protected DashboardItemDomain(Parcel in) {
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.value = in.readString();
        this.itemClicked = in.readString();
        this.itemSold = in.readString();
        this.isActive = in.readByte() != 0;
    }

    public static final Parcelable.Creator<DashboardItemDomain> CREATOR = new Parcelable.Creator<DashboardItemDomain>() {
        @Override
        public DashboardItemDomain createFromParcel(Parcel source) {
            return new DashboardItemDomain(source);
        }

        @Override
        public DashboardItemDomain[] newArray(int size) {
            return new DashboardItemDomain[size];
        }
    };
}
