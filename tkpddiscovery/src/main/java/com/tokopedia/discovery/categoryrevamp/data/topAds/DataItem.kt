package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class DataItem(

        @field:SerializedName("redirect")
        val redirect: String? = null,

        @field:SerializedName("sticker_id")
        val stickerId: String? = null,

        @field:SerializedName("product")
        val product: Product? = null,

        @field:SerializedName("shop")
        val shop: Shop = Shop(),

        @field:SerializedName("sticker_image")
        val stickerImage: String? = null,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("product_wishlist_url")
        val productWishlistUrl: String? = null,

        @field:SerializedName("shop_click_url")
        val shopClickUrl: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("ad_ref_key")
        val adRefKey: String? = null,

        @field:SerializedName("product_click_url")
        val productClickUrl: String? = null
)