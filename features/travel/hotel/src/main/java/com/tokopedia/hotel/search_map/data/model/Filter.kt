package com.tokopedia.hotel.search_map.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Filter(
        @SerializedName("filterPrice")
        @Expose
        val price: FilterPrice = FilterPrice(),

        @SerializedName("filterAccomodation")
        @Expose
        val accomodation: List<FilterAccomodation> = listOf(),

        @SerializedName("filterPreferences")
        @Expose
        val preferences: List<FilterPreference> = listOf(),

        @SerializedName("filterStar")
        @Expose
        val filterStar: FilterStar = FilterStar(),

        @SerializedName("filterReview")
        @Expose
        val filterReview: FilterReview = FilterReview()
) {
    data class FilterStar(
            @SerializedName("stars")
            @Expose
            val stars: List<Int> = (1..5).toList()
    )

    data class FilterReview(
            @SerializedName("minReview")
            @Expose
            val minReview: Float = 1f,

            @SerializedName("maxReview")
            @Expose
            val maxReview: Float = 10f
    )

    data class FilterPrice(
            @SerializedName("minPrice")
            @Expose
            val minPrice: Int = 0,

            @SerializedName("maxPrice")
            @Expose
            val maxPrice: Int = 0
    )

    data class FilterAccomodation(
            @SerializedName("ID")
            @Expose
            val id: Int = 0,

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("displayName")
            @Expose
            val displayName: String = ""
    )

    data class FilterPreference(
            @SerializedName("ID")
            @Expose
            val id: Int = 0,

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("displayName")
            @Expose
            val displayName: String = "",

            @SerializedName("type")
            @Expose
            val type: String = ""
    )
}