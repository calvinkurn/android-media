package com.tokopedia.hotel.homepage.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/05/20
 */

data class HotelPropertyDefaultHome(
        @SerializedName("label")
        @Expose
        val label: String = "",

        @SerializedName("searchType")
        @Expose
        val searchType: String = "",

        @SerializedName("searchID")
        @Expose
        val searchId: String = "",

        @SerializedName("checkIn")
        @Expose
        val checkIn: String = "",

        @SerializedName("checkOut")
        @Expose
        val checkOut: String = "",

        @SerializedName("totalRoom")
        @Expose
        val totalRoom: Int = 0,

        @SerializedName("totalGuest")
        @Expose
        val totalGuest: Int = 0
) {
    data class Response(
            @SerializedName("propertyDefaultHome")
            @Expose
            val response: PropertyDefaultHomeMetaAndData = PropertyDefaultHomeMetaAndData()
    )

    data class PropertyDefaultHomeMetaAndData(
            @SerializedName("data")
            @Expose
            val data: HotelPropertyDefaultHome = HotelPropertyDefaultHome(),

            @SerializedName("meta")
            @Expose
            val meta: PropertyDefaultHomeMeta = PropertyDefaultHomeMeta()
    )

    data class PropertyDefaultHomeMeta(
            @SerializedName("source")
            @Expose
            val source: String = ""
    )
}