package com.tokopedia.travel.passenger.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 03/01/2020
 */
data class TravelContactIdCard (
        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("number")
        @Expose
        val number: String = "",

        @SerializedName("country")
        @Expose
        val country: String = "",

        @SerializedName("expiry")
        @Expose
        val expiry: String = ""
)