package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 12/6/17.
 */

class PassengerEntity(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("type")
        @Expose
        val type: Int = 0,
        @SerializedName("title")
        @Expose
        val title: Int = 0,
        @SerializedName("first_name")
        @Expose
        val firstName: String = "",
        @SerializedName("last_name")
        @Expose
        val lastName: String = "",
        @SerializedName("dob")
        @Expose
        val dob: String = "",
        @SerializedName("nationality")
        @Expose
        val nationality: String = "",
        @SerializedName("passport_no")
        @Expose
        val passportNo: String = "",
        @SerializedName("passport_country")
        @Expose
        val passportCountry: String = "",
        @SerializedName("passport_expiry")
        @Expose
        val passportExpiry: String = "",
        @SerializedName("amenities")
        @Expose
        val amenities: List<PassengerAmentityEntity>)
