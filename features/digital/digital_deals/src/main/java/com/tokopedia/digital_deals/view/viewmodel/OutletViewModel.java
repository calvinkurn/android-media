package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletViewModel implements Parcelable {


    private int id;
    private int productId;
    private int locationId;
    private String name;
    private String searchName;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String district;
    private String gmapAddress;
    private String neighbourhood;
    private String coordinates;
    private String state;
    private String country;
    private int isSearchable;
    private int locationStatus;
    public final static Creator<OutletViewModel> CREATOR = new Creator<OutletViewModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OutletViewModel createFromParcel(Parcel in) {
            return new OutletViewModel(in);
        }

        public OutletViewModel[] newArray(int size) {
            return (new OutletViewModel[size]);
        }

    };

    protected OutletViewModel(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.locationId = in.readInt();
        this.isSearchable = in.readInt();
        this.locationStatus = in.readInt();
        this.name = in.readString();
        this.searchName = in.readString();
        this.metaTitle = in.readString();
        this.metaDescription = in.readString();
        this.metaKeywords = in.readString();
        this.district = in.readString();
        this.gmapAddress = in.readString();
        this.neighbourhood = in.readString();
        this.coordinates = in.readString();
        this.state = in.readString();
        this.country = in.readString();
    }

    public OutletViewModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
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

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGmapAddress() {
        return gmapAddress;
    }

    public void setGmapAddress(String gmapAddress) {
        this.gmapAddress = gmapAddress;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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

    public int getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(int locationStatus) {
        this.locationStatus = locationStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(productId);
        dest.writeInt(locationId);
        dest.writeInt(isSearchable);
        dest.writeInt(locationStatus);
        dest.writeString(name);
        dest.writeString(searchName);
        dest.writeString(metaTitle);
        dest.writeString(metaDescription);
        dest.writeString(metaKeywords);
        dest.writeString(district);
        dest.writeString(gmapAddress);
        dest.writeString(neighbourhood);
        dest.writeString(coordinates);
        dest.writeString(state);
        dest.writeString(country);

    }

    public int describeContents() {
        return 0;
    }

}