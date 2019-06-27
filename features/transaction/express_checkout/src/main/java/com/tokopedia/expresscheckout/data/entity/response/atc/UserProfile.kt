package com.tokopedia.expresscheckout.data.entity.response.atc

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class UserProfile(

        @SerializedName("profile_id")
        val code: Int,

        @SerializedName("status")
        val message: Int,

        @SerializedName("address_id")
        val addressId: Int,

        @SerializedName("receiver_name")
        val receiverName: String,

        @SerializedName("address_street")
        val addressStreet: String,

        @SerializedName("district_name")
        val districtName: String,

        @SerializedName("city_name")
        val cityName: String,

        @SerializedName("province_name")
        val provinceName: String,

        @SerializedName("phone_no")
        val phoneNo: String,

        @SerializedName("gateway_code")
        val gatewayCode: String,

        @SerializedName("checkout_param")
        val checkoutParam: String,

        @SerializedName("image")
        val image: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("url")
        val url: String,

        @SerializedName("service_id")
        val serviceId: Int

)