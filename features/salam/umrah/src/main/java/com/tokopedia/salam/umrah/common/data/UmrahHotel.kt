package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.SerializedName

/**
 * @author by M on 31/10/2019
 */
data class UmrahHotel(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        var name: String = "",

        @SerializedName("checkIn")
        var checkIn: String = "",

        @SerializedName("checkOut")
        var checkOut: String = "",

        @SerializedName("rating")
        var rating: Int = 0,

        @SerializedName("ui")
        var ui: UI = UI(),

        @SerializedName("imageUrl")
        var imageUrls: List<String> = listOf()
) {
    data class UI(
            @SerializedName("distance")
            var distance: String = ""
    )
}