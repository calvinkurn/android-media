package com.tokopedia.similarsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class Product(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("image_url_700")
        @Expose
        val imageUrl700: String = "",

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("shop")
        @Expose
        val shop: Shop = Shop(),

        @SerializedName("badges")
        @Expose
        val badgeList: List<Badge> = listOf(),

        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,

        @SerializedName("category_name")
        @Expose
        val categoryName: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Int = 0,

        @SerializedName("count_review")
        @Expose
        val countReview: Int = 0,

        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",

        @SerializedName("discount_expired_time")
        @Expose
        val discountExpiredTime: String = "",

        @SerializedName("discount_start_time")
        @Expose
        val discountStartTime: String = "",

        @SerializedName("discount_percentage")
        @Expose
        val discountPercentage: Int = 0,

        @SerializedName("wishlist")
        @Expose
        var isWishlisted: Boolean = false
)