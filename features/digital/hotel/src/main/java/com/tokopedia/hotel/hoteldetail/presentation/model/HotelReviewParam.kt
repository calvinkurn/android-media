package com.tokopedia.hotel.hoteldetail.presentation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 29/04/19
 */

data class HotelReviewParam(
        @SerializedName("propertyId")
        @Expose
        var propertyId: Int = 0,

        @SerializedName("page")
        @Expose
        var page: Int = 0,

        @SerializedName("rows")
        @Expose
        var rows: Int = 0,

        @SerializedName("sortBy")
        @Expose
        var sortBy: String = "",

        @SerializedName("sortType")
        @Expose
        var sortType: String = ""
)