package com.tokopedia.travel_slice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/10/20
 */

data class HotelParam(
        @SerializedName("location")
        @Expose
        var location: ParamLocation = ParamLocation(),

        @SerializedName("filter")
        @Expose
        var filter: ParamFilter = ParamFilter(),

        @SerializedName("filters")
        @Expose
        var filters: MutableList<Any> = mutableListOf(),

        @SerializedName("sort")
        @Expose
        var sort: ParamSort = ParamSort(),

        @SerializedName("checkIn")
        @Expose
        var checkIn: String = "",

        @SerializedName("checkOut")
        @Expose
        var checkOut: String = "",

        @SerializedName("room")
        @Expose
        var room: Int = 0,

        @SerializedName("guest")
        @Expose
        var guest: ParamGuest = ParamGuest(),

        @SerializedName("page")
        @Expose
        var page: Int = 1,

        @SerializedName("language")
        @Expose
        var language: String = "ID"
) {
    data class ParamLocation(
            @SerializedName("cityID")
            @Expose
            var cityID: Long = 0,

            @SerializedName("districtID")
            @Expose
            var districtID: Long = 0,

            @SerializedName("regionID")
            @Expose
            var regionID: Long = 0,

            @SerializedName("latitude")
            @Expose
            var latitude: Float = 0f,

            @SerializedName("longitude")
            @Expose
            var longitude: Float = 0f,

            @SerializedName("searchType")
            @Expose
            var searchType: String = "region",

            @SerializedName("searchID")
            @Expose
            var searchId: String= "4712"
    )

    data class ParamFilter(
            @SerializedName("departurePeriod")
            @Expose
            var departurePeriod: String = "-",

            @SerializedName("departureCity")
            @Expose
            var departureCity: String = "-",

            @SerializedName("minPeriod")
            @Expose
            var durationDaysMinimum: Int = 0,

            @SerializedName("maxPeriod")
            @Expose
            var durationDaysMaximum: Int = 0,

            @SerializedName("minPrice")
            @Expose
            var priceMinimum: Int = 0,

            @SerializedName("maxPrice")
            @Expose
            var priceMaximum: Int = 0
    )

    data class ParamSort(
            @SerializedName("popularity")
            @Expose
            var popularity: Boolean = false,

            @SerializedName("price")
            @Expose
            var price: Boolean = false,

            @SerializedName("ranking")
            @Expose
            var ranking: Boolean = false,

            @SerializedName("reviewScore")
            @Expose
            var reviewScore: Boolean = false,

            @SerializedName("star")
            @Expose
            var star: Boolean = false,

            @SerializedName("sortDir")
            @Expose
            var sortDir: String = "desc"
    )

    data class ParamGuest(
            @SerializedName("adult")
            @Expose
            var adult: Int = 1,

            @SerializedName("childAge")
            @Expose
            var childAge: List<Int> = listOf()
    )
}