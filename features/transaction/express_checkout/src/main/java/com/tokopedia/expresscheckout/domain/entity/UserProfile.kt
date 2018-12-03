package com.tokopedia.expresscheckout.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class UserProfile(

        @SerializedName("profile_id")
        @Expose
        val code: Int,

        @SerializedName("status")
        @Expose
        val message: Int,

        @SerializedName("address_id")
        @Expose
        val addressId: Int,

        @SerializedName("receiver_name")
        @Expose
        val receiverName: String,

        @SerializedName("address_street")
        @Expose
        val addressStreet: String,

        @SerializedName("district_name")
        @Expose
        val districtName: String,

        @SerializedName("city_name")
        @Expose
        val cityName: String,

        @SerializedName("province_name")
        @Expose
        val provinceName: String,

        @SerializedName("phone_no")
        @Expose
        val phoneNo: String,

        @SerializedName("gateway_code")
        @Expose
        val gatewayCode: String,

        @SerializedName("checkout_param")
        @Expose
        val checkoutParam: String,

        @SerializedName("image")
        @Expose
        val image: String,

        @SerializedName("description")
        @Expose
        val description: String,

        @SerializedName("url")
        @Expose
        val url: String,

        @SerializedName("service_id")
        @Expose
        val serviceId: Int

)