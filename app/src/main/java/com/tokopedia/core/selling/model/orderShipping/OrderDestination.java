
package com.tokopedia.core.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderDestination {

    @SerializedName("receiver_phone_is_tokopedia")
    @Expose
    Integer receiverPhoneIsTokopedia;
    @SerializedName("receiver_name")
    @Expose
    String receiverName;
    @SerializedName("address_country")
    @Expose
    String addressCountry;
    @SerializedName("address_postal")
    @Expose
    String addressPostal;
    @SerializedName("address_district")
    @Expose
    String addressDistrict;
    @SerializedName("receiver_phone")
    @Expose
    String receiverPhone;
    @SerializedName("address_street")
    @Expose
    String addressStreet;
    @SerializedName("address_city")
    @Expose
    String addressCity;
    @SerializedName("address_province")
    @Expose
    String addressProvince;

    /**
     * 
     * @return
     *     The receiverPhoneIsTokopedia
     */
    public Integer getReceiverPhoneIsTokopedia() {
        return receiverPhoneIsTokopedia;
    }

    /**
     * 
     * @param receiverPhoneIsTokopedia
     *     The receiver_phone_is_tokopedia
     */
    public void setReceiverPhoneIsTokopedia(Integer receiverPhoneIsTokopedia) {
        this.receiverPhoneIsTokopedia = receiverPhoneIsTokopedia;
    }

    /**
     * 
     * @return
     *     The receiverName
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 
     * @param receiverName
     *     The receiver_name
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 
     * @return
     *     The addressCountry
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * 
     * @param addressCountry
     *     The address_country
     */
    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

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
     *     The receiverPhone
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 
     * @param receiverPhone
     *     The receiver_phone
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    /**
     * 
     * @return
     *     The addressStreet
     */
    public String getAddressStreet() {
        return addressStreet;
    }

    /**
     * 
     * @param addressStreet
     *     The address_street
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
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
