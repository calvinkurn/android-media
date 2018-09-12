package com.tokopedia.tokopoints.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogCategory implements Parcelable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("imageID")
    private String imageId;

    @Expose
    @SerializedName("imageURL")
    private String imageUrl;

    @Expose
    @SerializedName("index")
    private int index;

    @Expose
    @SerializedName("timeRemainingSeconds")
    private long timeRemainingSeconds;

    private long timeWithCurrentMs;

    @Expose
    @SerializedName("isSelected")
    private boolean isSelected;

    public CatalogCategory() {

    }

    public long getTimeWithCurrentMs() {
        return timeWithCurrentMs;
    }

    public void setTimeWithCurrentMs(long timeWithCurrentMs) {
        this.timeWithCurrentMs = timeWithCurrentMs;
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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "CatalogCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", index=" + index +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(index);
        parcel.writeString(name);
        parcel.writeString(imageId);
        parcel.writeString(imageUrl);
        parcel.writeLong(timeRemainingSeconds);
        parcel.writeLong(timeWithCurrentMs);
    }

    private CatalogCategory(Parcel in) {
        this.id = in.readInt();
        index = in.readInt();
        name = in.readString();
        imageId = in.readString();
        imageUrl = in.readString();
        timeRemainingSeconds = in.readLong();
        timeWithCurrentMs = in.readLong();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CatalogCategory createFromParcel(Parcel in) {
            return new CatalogCategory(in);
        }

        public CatalogCategory[] newArray(int size) {
            return new CatalogCategory[size];
        }
    };
}
