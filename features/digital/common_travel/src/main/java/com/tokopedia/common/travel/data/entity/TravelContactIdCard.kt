package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-09-02
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