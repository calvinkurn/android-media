package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TopAdslistProduct(
    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("image")
    @Expose
    val image: TopAdslistImage = TopAdslistImage(),

    @SerializedName("uri")
    @Expose
    val uri: String = "",

    @SerializedName("relative_uri")
    @Expose
    val relativeUri: String = "",

    @SerializedName("price_format")
    @Expose
    val priceFormat: String = "",

    @SerializedName("count_talk_format")
    @Expose
    val countTalkFormat: String = "",

    @SerializedName("count_review_format")
    @Expose
    val countReviewFormat: String = "",

    @SerializedName("category")
    @Expose
    val category: TopAdsCategoryType = TopAdsCategoryType(),

    @SerializedName("product_preorder")
    @Expose
    val productPreorder: Boolean = false,

    @SerializedName("product_wholesale")
    @Expose
    val productWholesale: Boolean = false,

    @SerializedName("free_return")
    @Expose
    val freeReturn: String = "",

    @SerializedName("product_cashback")
    @Expose
    val productCashback: Boolean = false,

    @SerializedName("product_cashback_rate")
    @Expose
    val productCashbackRate: String = "",

    @SerializedName("product_rating")
    @Expose
    val productRating: Int = 0
)
