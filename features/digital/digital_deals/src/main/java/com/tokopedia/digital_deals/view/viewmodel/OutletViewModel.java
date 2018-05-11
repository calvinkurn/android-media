package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletViewModel implements Parcelable {


    private Integer id;
    private Integer productId;
    private Integer locationId;
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
    private Integer isSearchable;
    private Integer locationStatus;
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
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.productId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.searchName = ((String) in.readValue((String.class.getClassLoader())));
        this.metaTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.metaDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.metaKeywords = ((String) in.readValue((String.class.getClassLoader())));
        this.district = ((String) in.readValue((String.class.getClassLoader())));
        this.gmapAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.neighbourhood = ((String) in.readValue((String.class.getClassLoader())));
        this.coordinates = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.isSearchable = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationStatus = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public OutletViewModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
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

    public Integer getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(Integer isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Integer getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(Integer locationStatus) {
        this.locationStatus = locationStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(productId);
        dest.writeValue(locationId);
        dest.writeValue(name);
        dest.writeValue(searchName);
        dest.writeValue(metaTitle);
        dest.writeValue(metaDescription);
        dest.writeValue(metaKeywords);
        dest.writeValue(district);
        dest.writeValue(gmapAddress);
        dest.writeValue(neighbourhood);
        dest.writeValue(coordinates);
        dest.writeValue(state);
        dest.writeValue(country);
        dest.writeValue(isSearchable);
        dest.writeValue(locationStatus);
    }

    public int describeContents() {
        return 0;
    }

}