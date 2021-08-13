package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoAddress {
    @SerializedName("location_address")
    @Expose
    var locationAddress: String? = null

    @SerializedName("location_address_id")
    @Expose
    var locationAddressId: String? = null

    @SerializedName("location_address_name")
    @Expose
    var locationAddressName: String? = null

    @SerializedName("location_area")
    @Expose
    var locationArea: String? = null

    @SerializedName("location_city_id")
    @Expose
    var locationCityId: String? = null

    @SerializedName("location_city_name")
    @Expose
    var locationCityName: String? = null

    @SerializedName("location_district_id")
    @Expose
    var locationDistrictId: String? = null

    @SerializedName("location_district_name")
    @Expose
    var locationDistrictName: String? = null

    @SerializedName("location_email")
    @Expose
    var locationEmail: String? = null

    @SerializedName("location_fax")
    @Expose
    var locationFax: String? = null

    @SerializedName("location_phone")
    @Expose
    var locationPhone: String? = null

    @SerializedName("location_postal_code")
    @Expose
    var locationPostalCode: String? = null

    @SerializedName("location_province_id")
    @Expose
    var locationProvinceId: String? = null

    @SerializedName("location_province_name")
    @Expose
    var locationProvinceName: String? = null
}