package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem

/**
 * @author by astidhiyaa on 29/08/22
 */
data class ProductFeedType(
    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("price")
    @Expose
    val price: String = "",

    @SerializedName("price_int")
    @Expose
    val priceInt: String = "",

    @SerializedName("price_original")
    @Expose
    val priceOriginal: String = "",

    @SerializedName("price_original_int")
    @Expose
    val priceOriginalInt: String = "",

    @SerializedName("image")
    @Expose
    val image: String = "",

    @SerializedName("image_single")
    @Expose
    val imageSingle: String = "",

    @SerializedName("wholesale")
    @Expose
    val wholesale: List<Wholesale> = emptyList(),

    @SerializedName("freereturns")
    @Expose
    val freereturns: Boolean = false,

    @SerializedName("preorder")
    @Expose
    val preorder: Boolean = false,

    @SerializedName("cashback")
    @Expose
    val cashback: String = "",

    @SerializedName("url")
    @Expose
    val url: String = "",

    @SerializedName("productLink")
    @Expose
    val productLink: String = "",

    @SerializedName("wishlist")
    @Expose
    val wishlist: Boolean = false,

    @SerializedName("rating")
    @Expose
    val rating: Float = 0f,

    @SerializedName("countReview")
    @Expose
    val countReview: String = "",

    @SerializedName("tags")
    @Expose
    val tags: List<TagsItem> = emptyList()
)
