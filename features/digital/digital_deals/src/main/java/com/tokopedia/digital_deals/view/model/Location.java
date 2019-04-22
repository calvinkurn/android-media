package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("search_name")
    @Expose
    private String searchName;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("is_searchable")
    @Expose
    private int isSearchable;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_app")
    @Expose
    private String imageApp;


    public final static Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
        }

    };

    protected Location(Parcel in) {
        this.id = in.readInt();
        this.categoryId = in.readInt();
        this.name = in.readString();
        this.searchName = in.readString();
        this.district = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.isSearchable = in.readInt();
        this.status = in.readInt();
        this.icon = in.readString();
        this.url = in.readString();
        this.imageApp = in.readString();
    }

    public Location() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(int isSearchable) {
        this.isSearchable = isSearchable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(categoryId);
        dest.writeString(name);
        dest.writeString(searchName);
        dest.writeString(district);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeInt(isSearchable);
        dest.writeInt(status);
        dest.writeString(icon);
        dest.writeString(url);
        dest.writeString(imageApp);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String str = name + "  " + id;
        return str;
    }
}
