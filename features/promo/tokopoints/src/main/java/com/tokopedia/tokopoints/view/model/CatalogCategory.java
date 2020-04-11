package com.tokopedia.tokopoints.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogCategory implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("parentID")
    private int parentID;

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

    @SerializedName("isHideSubCategory")
    private boolean isHideSubCategory;

    @SerializedName("subCategory")
    List<CatalogSubCategory> subCategory;

    public CatalogCategory() {

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

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isHideSubCategory() {
        return isHideSubCategory;
    }

    public void setHideSubCategory(boolean hideSubCategory) {
        isHideSubCategory = hideSubCategory;
    }

    public List<CatalogSubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<CatalogSubCategory> subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "CatalogCategory{" +
                "id=" + id +
                ", parentID=" + parentID +
                ", name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", slug='" + slug + '\'' +
                ", timeRemainingSeconds=" + timeRemainingSeconds +
                ", isSelected=" + isSelected +
                ", isHideSubCategory=" + isHideSubCategory +
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
        parcel.writeInt(parentID);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeByte((byte) (isHideSubCategory ? 1 : 0));
    }

    private CatalogCategory(Parcel in) {
        this.id = in.readInt();
        name = in.readString();
        imageId = in.readString();
        imageUrl = in.readString();
        timeRemainingSeconds = in.readLong();
        isSelected = in.readByte() != 0;
        isHideSubCategory = in.readByte() != 0;
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
