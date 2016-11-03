
package com.tokopedia.tkpd.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderShop {

    @SerializedName("address_postal")
    @Expose
    String addressPostal;
    @SerializedName("address_district")
    @Expose
    String addressDistrict;
    @SerializedName("address_city")
    @Expose
    String addressCity;
    @SerializedName("address_street")
    @Expose
    String addressStreet;
    @SerializedName("shipper_phone")
    @Expose
    String shipperPhone;
    @SerializedName("address_country")
    @Expose
    String addressCountry;
    @SerializedName("address_province")
    @Expose
    String addressProvince;

    /**
     * 
     * @return
     *     The addressPostal
     */
    public String getAddressPostal() {
        return addressPostal;
    }

    /**
     * 
     * @param addressPostal
     *     The address_postal
     */
    public void setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
    }

    /**
     * 
     * @return
     *     The addressDistrict
     */
    public String getAddressDistrict() {
        return addressDistrict;
    }

    /**
     * 
     * @param addressDistrict
     *     The address_district
     */
    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    /**
     * 
     * @return
     *     The addressCity
     */
    public String getAddressCity() {
        return addressCity;
    }

    /**
     * 
     * @param addressCity
     *     The address_city
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * 
     * @return
     *     The shipperPhone
     */
    public String getShipperPhone() {
        return shipperPhone;
    }

    /**
     * 
     * @param shipperPhone
     *     The shipper_phone
     */
    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    /**
     * 
     * @return
     *     The addressProvince
     */
    public String getAddressProvince() {
        return addressProvince;
    }

    /**
     * 
     * @param addressProvince
     *     The address_province
     */
    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

}
