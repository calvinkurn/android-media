
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class OrderShop {

    @SerializedName("address_postal")
    @Expose
    private String addressPostal;
    @SerializedName("address_district")
    @Expose
    private String addressDistrict;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("shipper_phone")
    @Expose
    private String shipperPhone;
    @SerializedName("address_country")
    @Expose
    private int addressCountry;
    @SerializedName("address_province")
    @Expose
    private String addressProvince;

    public String getAddressPostal() {
        return addressPostal;
    }

    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public int getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(int addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

}
