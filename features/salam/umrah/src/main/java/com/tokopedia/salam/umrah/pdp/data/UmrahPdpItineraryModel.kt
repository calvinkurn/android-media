package com.tokopedia.salam.umrah.pdp.data

import com.google.gson.annotations.SerializedName
/**
 * @author by M on 30/10/19
 */
data class UmrahPdpItineraryModel(
        @SerializedName("day")
        var day: Int = 0,

        @SerializedName("description")
        var desc: String = ""
)