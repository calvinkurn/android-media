package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.hoteldetail.presentation.adapter.ReviewAdapterTypeFactory

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
        val createTime: String = "",

        @SerializedName("country")
        @Expose
        val country: String = ""

) : Visitable<ReviewAdapterTypeFactory> {

    override fun type(typeFactory: ReviewAdapterTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    data class Response(
            @SerializedName("propertyReview")
            @Expose
            val propertyReview: ReviewData = ReviewData()
    )

    data class ReviewData(
            @SerializedName("item")
            @Expose
            val reviewList: List<HotelReview> = listOf(),

            @SerializedName("totalReview")
            @Expose
            val totalReview: Int = 0,

            @SerializedName("averageScoreReview")
            @Expose
            val averageScoreReview: Float = 0f,

            @SerializedName("hasNext")
            @Expose
            val hasNext: Boolean = true,

            @SerializedName("headline")
            @Expose
            val headline: String = ""
    )
}