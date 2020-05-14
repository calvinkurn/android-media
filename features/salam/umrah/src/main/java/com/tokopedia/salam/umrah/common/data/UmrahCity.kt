package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.SerializedName
/**
 * @author by M on 31/10/2019
 */
data class UmrahCity(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        var name: String = "",

        @SerializedName("countryCode")
        var countryCode: String = ""
)