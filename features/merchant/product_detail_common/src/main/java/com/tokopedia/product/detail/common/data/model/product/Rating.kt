package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Rating(
        @SerializedName("ratingScore")
        @Expose
        val ratingScore: String = "0.0",

        @SerializedName("totalRating")
        @Expose
        val totalRating: Int = 0,

        @SerializedName("detail")
        @Expose
        val detail: List<Detail> = listOf()
){

    data class Response(
            @SerializedName("ProductRatingQuery")
            @Expose
            val data: Rating = Rating()
    )

    data class Detail(
            @SerializedName("rate")
            @Expose
            val rate: Int = 0,

            @SerializedName("totalReviews")
            @Expose
            val totalReviews: Int = 0,

            @SerializedName("percentage")
            @Expose
            val percentage: String = "10")
}