package com.tokopedia.hotel.search_map.data.model

/**
 * @author by jessica on 22/04/20
 */

enum class HotelSortEnum(val value: String, val order: String) {
    POPULARITY("popularity", "desc"),
    PRICE("price", "asc"),
    RANKING("ranking", "desc"),
    STAR("star", "desc"),
    REVIEWSCORE("reviewscore", "desc")
}