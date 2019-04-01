package com.tokopedia.hotel.search.data

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
        val preferences: List<FilterPreference> = listOf()
) {

    data class FilterPrice(
            @SerializedName("minPrice")
            @Expose
            val minPrice: Float = 0f,

            @SerializedName("maxPrice")
            @Expose
            val maxPrice: Float = 0f
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