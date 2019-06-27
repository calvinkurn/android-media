package com.tokopedia.flashsale.management.data.campaignlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Criteria(
        @SerializedName("criteria_id")
        @Expose
        val id: Long = -1,

        @SerializedName("min_price")
        @Expose
        val minPrice: String = "",

        @SerializedName("max_price")
        @Expose
        val maxPrice: String = "",

        @SerializedName("min_stock")
        @Expose
        val minStock: Int = 0,

        @SerializedName("minimum_discount_percent")
        @Expose
        val minDiscount: Int = 0,

        @SerializedName("maximum_discount_percent")
        @Expose
        val maxDiscount: Int = 0,

        @SerializedName("minimum_cashback")
        @Expose
        val minCashback: Int = 0,

        @SerializedName("max_submission")
        @Expose
        val maxSubmission: Int = 0,

        @SerializedName("maximum_rating")
        @Expose
        val maxRating: Int = 0,

        @SerializedName("exclude_preoder")
        @Expose
        val isPreorderExcluded: Boolean = false,

        @SerializedName("exclude_wholesale")
        @Expose
        val isWholesaleExcluded: Boolean = false,

        @SerializedName("categories")
        @Expose
        val categories: List<Category> = listOf()
){

    data class Category(
            @SerializedName("dep_id")
            @Expose
            val depId: Long = -1,

            @SerializedName("dep_name")
            @Expose
            val depName: String = "",

            @SerializedName("dep_full_name")
            @Expose
            val depFullName: String = ""
    )
}