package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationViewModel implements Parcelable{



    private int id;
    private int categoryId;
    private String name;
    private String searchName;
    private String district;
    private String state;
    private String country;
    private int isSearchable;
    private int status;
    private String icon;
    private String url;

    public final static Parcelable.Creator<LocationViewModel> CREATOR = new Parcelable.Creator<LocationViewModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationViewModel createFromParcel(Parcel in) {
            return new LocationViewModel(in);
        }

        public LocationViewModel[] newArray(int size) {
            return (new LocationViewModel[size]);
        }

    };

    protected LocationViewModel(Parcel in) {
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
    }

    public LocationViewModel() {
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
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        String str=name+"  "+id;
        return str;
    }
}
