package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class UserAddress {
    @SerializedName("address_id")
    @Expose
    private int addressId;
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("province_id")
    @Expose
    private int provinceId;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("city_id")
    @Expose
    private int cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("district_id")
    @Expose
    private int districtId;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("address_2")
    @Expose
    private String address2;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("corner_id")
    @Expose
    private int cornerId;

    public int getAddressId() {
        return addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getStatus() {
        return status;
    }

    public String getCountry() {
        return country;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getDistrictId() {
        return districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getAddress2() {
        return address2;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }
}
