package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class Product(

        @field:SerializedName("image")
        val image: Image? = null,

        @field:SerializedName("price_format")
        val priceFormat: String? = null,

        @field:SerializedName("label_group")
        val labelGroup: List<LabelGroupItem?>? = null,

        @field:SerializedName("wishlist")
        val wishlist: Boolean = false,

        @field:SerializedName("product_new_label")
        val productNewLabel: Boolean? = null,

        @field:SerializedName("product_cashback")
        val productCashback: Boolean? = null,

        @field:SerializedName("count_talk_format")
        val countTalkFormat: String? = null,

        @field:SerializedName("free_return")
        val freeReturn: String? = null,

        @field:SerializedName("uri")
        val uri: String? = null,

        @field:SerializedName("count_review_format")
        val countReviewFormat: String? = null,

        @field:SerializedName("labels")
        val labels: List<LabelsItem?>? = null,

        @field:SerializedName("top_label")
        val topLabel: List<Any?>? = null,

        @field:SerializedName("product_cashback_rate")
        val productCashbackRate: String? = null,

        @field:SerializedName("relative_uri")
        val relativeUri: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("wholesale_price")
        val wholesalePrice: List<WholesalePriceItem?>? = null,

        @field:SerializedName("campaign")
        val campaign: Campaign? = null,

        @field:SerializedName("product_preorder")
        val productPreorder: Boolean? = null,

        @field:SerializedName("bottom_label")
        val bottomLabel: List<Any?>? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("category")
        val category: Category? = null,

        @field:SerializedName("product_wholesale")
        val productWholesale: Boolean? = null,

        @field:SerializedName("product_rating")
        val productRating: Int? = null,

        @field:SerializedName("free_ongkir")
        val freeOngkir: FreeOngkir? = null
)


data class FreeOngkir(
        @SerializedName("is_active")
        val isActive: Boolean,
        @SerializedName("img_url")
        val imageUrl: String
)