package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationViewModel implements Parcelable{



    private Integer id;
    private Integer categoryId;
    private String name;
    private String searchName;
    private String district;
    private String state;
    private String country;
    private Integer isSearchable;
    private Integer status;
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
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.categoryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.searchName = ((String) in.readValue((String.class.getClassLoader())));
        this.district = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.isSearchable = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LocationViewModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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

    public Integer getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(Integer isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
        dest.writeValue(id);
        dest.writeValue(categoryId);
        dest.writeValue(name);
        dest.writeValue(searchName);
        dest.writeValue(district);
        dest.writeValue(state);
        dest.writeValue(country);
        dest.writeValue(isSearchable);
        dest.writeValue(status);
        dest.writeValue(icon);
        dest.writeValue(url);
    }

    public int describeContents() {
        return 0;
    }

}
