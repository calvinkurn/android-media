
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Address {

    @SerializedName("location_city_name")
    @Expose
    public String locationCityName;
    @SerializedName("location_email")
    @Expose
    public String locationEmail;
    @SerializedName("location_address_name")
    @Expose
    public String locationAddressName;
    @SerializedName("location_postal_code")
    @Expose
    public String locationPostalCode;
    @SerializedName("location_address")
    @Expose
    public String locationAddress;
    @SerializedName("location_city_id")
    @Expose
    public String locationCityId;
    @SerializedName("location_phone")
    @Expose
    public String locationPhone;
    @SerializedName("location_area")
    @Expose
    public String locationArea;
    @SerializedName("location_district_id")
    @Expose
    public String locationDistrictId;
    @SerializedName("location_province_name")
    @Expose
    public String locationProvinceName;
    @SerializedName("location_province_id")
    @Expose
    public String locationProvinceId;
    @SerializedName("location_district_name")
    @Expose
    public String locationDistrictName;
    @SerializedName("location_fax")
    @Expose
    public String locationFax;
    @SerializedName("location_address_id")
    @Expose
    public String locationAddressId;

}
