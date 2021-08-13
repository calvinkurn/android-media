package com.tokopedia.travel_slice.hotel.data

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
        var room: Int = 1,

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
            @SerializedName("maxPrice")
            @Expose
            var maxPrice: Int = 0,

            @SerializedName("minPrice")
            @Expose
            var minPrice: Int = 0,

            @SerializedName("star")
            @Expose
            var star: List<Int> = listOf(),

            @SerializedName("paymentType")
            @Expose
            var paymentType: Int = 0,

            @SerializedName("mealPlan")
            @Expose
            var mealPlan: Int = 0,

            @SerializedName("reviewScore")
            @Expose
            var reviewScore: Int = 0,

            @SerializedName("hotelFacilities")
            @Expose
            var hotelFacilities: List<Int> = listOf(),

            @SerializedName("roomFacilities")
            @Expose
            var roomFacilities: List<Int> = listOf(),

            @SerializedName("dealType")
            @Expose
            var dealType: List<Int> = listOf(),

            @SerializedName("platformType")
            @Expose
            var platformType: Int = 0,

            @SerializedName("propertyType")
            @Expose
            var propertyType: List<Int> = listOf(),

            @SerializedName("cancellationPolicy")
            @Expose
            var cancellationPolicies: List<Int> = listOf()
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

data class SearchSuggestionParam(
        @SerializedName("searchKey")
        @Expose
        val searchKey: String = ""
)