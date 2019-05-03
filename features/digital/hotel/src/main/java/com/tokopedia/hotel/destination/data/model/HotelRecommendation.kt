package com.tokopedia.hotel.destination.data.model

/**
 * @author by jessica on 03/05/19
 */

data class HotelRecommendation(
        val hotelPopularSearch: List<PopularSearch> = listOf(),
        val hotelRecentSearch: List<RecentSearch> = listOf()
)