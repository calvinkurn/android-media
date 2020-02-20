package com.tokopedia.opportunity.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 27/03/18.
 */

public class OpportunityDetailDestination {
    @SerializedName("destination_country")
    @Expose
    private String destinationCountry;
    @SerializedName("destination_postal_code")
    @Expose
    private String destinationPostalCode;
    @SerializedName("destination_district")
    @Expose
    private String destinationDistrict;
    @SerializedName("destination_street")
    @Expose
    private String destinationStreet;
    @SerializedName("destination_city")
    @Expose
    private String destinationCity;
    @SerializedName("destination_province")
    @Expose
    private String destinationProvince;

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationPostalCode() {
        return destinationPostalCode;
    }

    public void setDestinationPostalCode(String destinationPostalCode) {
        this.destinationPostalCode = destinationPostalCode;
    }

    public String getDestinationDistrict() {
        return destinationDistrict;
    }

    public void setDestinationDistrict(String destinationDistrict) {
        this.destinationDistrict = destinationDistrict;
    }

    public String getDestinationStreet() {
        return destinationStreet;
    }

    public void setDestinationStreet(String destinationStreet) {
        this.destinationStreet = destinationStreet;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationProvince() {
        return destinationProvince;
    }

    public void setDestinationProvince(String destinationProvince) {
        this.destinationProvince = destinationProvince;
    }
}
