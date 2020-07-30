package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outlet implements Parcelable {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("location_id")
    @Expose
    private int locationId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("search_name")
    @Expose
    private String searchName;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription;
    @SerializedName("meta_keywords")
    @Expose
    private String metaKeywords;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("gmap_address")
    @Expose
    private String gmapAddress;
    @SerializedName("neighbourhood")
    @Expose
    private String neighbourhood;
    @SerializedName("coordinates")
    @Expose
    private String coordinates;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("is_searchable")
    @Expose
    private int isSearchable;
    @SerializedName("location_status")
    @Expose
    private int locationStatus;
    public final static Creator<Outlet> CREATOR = new Creator<Outlet>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Outlet createFromParcel(Parcel in) {
            return new Outlet(in);
        }

        public Outlet[] newArray(int size) {
            return (new Outlet[size]);
        }

    };

    protected Outlet(Parcel in) {
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

    public Outlet() {
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