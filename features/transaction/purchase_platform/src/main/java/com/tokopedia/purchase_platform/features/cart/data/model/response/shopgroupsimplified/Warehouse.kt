package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 13/03/19.
 */
data class Warehouse (
    @SerializedName("warehouse_id")
    @Expose
    var warehouseId: Int = 0,
    @SerializedName("partner_id")
    @Expose
    var partnerId: Int = 0,
    @SerializedName("shop_id")
    @Expose
    var shopId: Int = 0,
    @SerializedName("warehouse_name")
    @Expose
    var warehouseName: String = "",
    @SerializedName("district_id")
    @Expose
    var districtId: Int = 0,
    @SerializedName("district_name")
    @Expose
    var districtName: String = "",
    @SerializedName("city_id")
    @Expose
    var cityId: Int = 0,
    @SerializedName("city_name")
    @Expose
    var cityName: String = "",
    @SerializedName("province_id")
    @Expose
    var provinceId: Int = 0,
    @SerializedName("province_name")
    @Expose
    var provinceName: String = "",
    @SerializedName("status")
    @Expose
    var status: Int = 0,
    @SerializedName("postal_code")
    @Expose
    var postalCode: String = "",
    @SerializedName("is_default")
    @Expose
    var isDefault: Int = 0,
    @SerializedName("latlon")
    @Expose
    var latlon: String = "",
    @SerializedName("latitude")
    @Expose
    var latitude: String = "",
    @SerializedName("longitude")
    @Expose
    var longitude: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("address_detail")
    @Expose
    var addressDetail: String = "",
    @SerializedName("country_name")
    @Expose
    var countryName: String = ""
)
