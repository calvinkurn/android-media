package com.tokopedia.shop.common.graphql.data.shoplocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 08/08/18.
 */

data class ShopLocationModel (
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("address")
    @Expose
    val address: String = "",
    @SerializedName("districtId")
    @Expose
    val districtId: String = "",
    @SerializedName("districtName")
    @Expose
    val districtName: String = "",
    @SerializedName("cityId")
    @Expose
    val cityId: String = "",
    @SerializedName("cityName")
    @Expose
    val cityName: String = "",
    @SerializedName("stateId")
    @Expose
    val stateId: String = "",
    @SerializedName("stateName")
    @Expose
    val stateName: String = "",
    @SerializedName("postalCode")
    @Expose
    val postalCode: Int = -1,
    @SerializedName("email")
    @Expose
    val email: String = "",
    @SerializedName("phone")
    @Expose
    val phone: String = "",
    @SerializedName("fax")
    @Expose
    val fax: String = ""
)
