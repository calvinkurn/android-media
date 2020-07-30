package com.tokopedia.tokopoints.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CatalogSubCategory implements Parcelable {
    @SerializedName("id")
    private int id;



    @SerializedName("name")
    private String name;

    @SerializedName("imageID")
    private String imageId;

    @SerializedName("imageURL")
    private String imageUrl;

    @SerializedName("slug")
    private String slug;

    @SerializedName("timeRemainingSeconds")
    private long timeRemainingSeconds;

    @SerializedName("isSelected")
    private boolean isSelected;

    @SerializedName("timeRemainingSecondsLabel")
    private String timerLabel;

    public CatalogSubCategory() {

    }

    public String getTimerLabel() {
        return timerLabel;
    }

    public void setTimerLabel(String timerLabel) {
        this.timerLabel = timerLabel;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void setTimeRemainingSeconds(long timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "CatalogCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", slug='" + slug + '\'' +
                ", timeRemainingSeconds=" + timeRemainingSeconds +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(imageId);
        parcel.writeString(imageUrl);
        parcel.writeLong(timeRemainingSeconds);
        parcel.writeString(slug);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }

    private CatalogSubCategory(Parcel in) {
        this.id = in.readInt();
        name = in.readString();
        imageId = in.readString();
        imageUrl = in.readString();
        timeRemainingSeconds = in.readLong();
        isSelected = in.readByte() != 0;
    }

    public static final Creator CREATOR = new Creator() {
        public CatalogSubCategory createFromParcel(Parcel in) {
            return new CatalogSubCategory(in);
        }

        public CatalogSubCategory[] newArray(int size) {
            return new CatalogSubCategory[size];
        }
    };
}
