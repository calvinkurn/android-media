package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 01/04/19
 */

data class HotelRecommendation (
        @SerializedName("recent_search")
        @Expose
        val recentSearchList: List<RecentSearch> = listOf(),

        @SerializedName("propertyPopular")
        @Expose
        val popularSearchList: List<PopularSearch> = listOf()
) {

        data class Response (
            @SerializedName("data")
            @Expose
            val dataHotelRecommendation: HotelRecommendation = HotelRecommendation(),

            @Expose
            @SerializedName("errors")
            val errors: List<Error> = listOf())
}