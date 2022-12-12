package com.tokopedia.hotel.hoteldetail.presentation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 29/04/19
 */

data class HotelReviewParam(
    @SerializedName("propertyId")
    @Expose
    var propertyId: Long = 0,

    @SerializedName("page")
    @Expose
    var page: Int = 0,

    @SerializedName("rows")
    @Expose
    var rows: Int = 11,

    @SerializedName("sortBy")
    @Expose
    var sortBy: String = "create_time",

    @SerializedName("sortType")
    @Expose
    var sortType: String = "desc",

    @SerializedName("filterByCountry")
    @Expose
    var filterByCountry: String = "id",

    @SerializedName("filterByRank")
    @Expose
    var filterByRank: Int = 0
)
