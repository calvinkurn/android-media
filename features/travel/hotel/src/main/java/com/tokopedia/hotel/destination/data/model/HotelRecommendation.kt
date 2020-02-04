package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 03/05/19
 */

data class HotelRecommendation(
        @SerializedName("propertyPopular")
        @Expose
        val popularSearchList: List<PopularSearch> = listOf(),

        @SerializedName("travelRecentSearch")
        @Expose
        val recentSearch: List<RecentSearch> = listOf()
)