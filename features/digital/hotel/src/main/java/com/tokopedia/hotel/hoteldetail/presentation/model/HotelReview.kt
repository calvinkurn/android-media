package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 29/04/19
 */

data class HotelReview(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("propertyId")
        @Expose
        val propertyId: Int = 0,

        @SerializedName("reviewerId")
        @Expose
        val reviewerId: Int = 0,

        @SerializedName("name")
        @Expose
        val reviewerName: String = "",

        @SerializedName("score")
        @Expose
        val score: Int = 0,

        @SerializedName("headline")
        @Expose
        val headline: String = "",

        @SerializedName("pros")
        @Expose
        val pros: String = "",

        @SerializedName("cons")
        @Expose
        val cons: String = "",

        @SerializedName("createTime")
        @Expose
        val createTime: String = ""
) {
    data class Response(
            @SerializedName("propertyReview")
            @Expose
            val propertyReview: ReviewList
    )

    data class ReviewList(
            @SerializedName("item")
            @Expose
            val reviewList: List<HotelReview> = listOf()
    )
}