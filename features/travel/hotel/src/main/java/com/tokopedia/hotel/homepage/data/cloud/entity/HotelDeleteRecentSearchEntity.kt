package com.tokopedia.hotel.homepage.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 14/01/2020
 */
class HotelDeleteRecentSearchEntity(@SerializedName("Result")
                               @Expose
                               val result: Boolean = true) {
    class Response(@SerializedName("travelRecentSearchDelete")
                   @Expose
                   val travelRecentSearchHotelDelete: HotelDeleteRecentSearchEntity = HotelDeleteRecentSearchEntity())
}
