package com.tokopedia.salam.umrah.pdp.data

import com.google.gson.annotations.SerializedName

/**
 * @author by M on 30/10/19
 */
data class UmrahPdpFeaturedFacilityModel(
        @SerializedName("iconUrl")
        var iconUrl: String = "",

        @SerializedName("iconDrawable")
        var iconDrawable: Int = 0,

        @SerializedName("header")
        val header: String = ""
)