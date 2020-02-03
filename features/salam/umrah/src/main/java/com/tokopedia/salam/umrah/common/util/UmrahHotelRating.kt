package com.tokopedia.salam.umrah.common.util

import com.tokopedia.salam.umrah.common.data.UmrahHotel
/**
 * @author by M on 31/10/2019
 */
object UmrahHotelRating {
    fun getAllHotelRatings(hotels: List<UmrahHotel>): String {
        val ratings = mutableListOf<Int>()
        for (hotel in hotels){
            ratings.add(hotel.rating)
        }
        val showedRatings = ratings.distinct()
        val all: String
        all = when (showedRatings.size) {
            1 -> "${showedRatings[0]}"
            2 -> "${showedRatings[0]} & ${showedRatings[1]}"
            else -> "${showedRatings[0]} - ${showedRatings[showedRatings.size-1]}"
        }
        return all
    }
}