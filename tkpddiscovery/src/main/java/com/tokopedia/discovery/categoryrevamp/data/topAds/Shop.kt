package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.categoryrevamp.data.productModel.BadgesItem

data class Shop(

        @field:SerializedName("badges")
        val badges: List<BadgesItem>? = null,

        @field:SerializedName("lucky_shop")
        val luckyShop: String? = null,

        @field:SerializedName("gold_shop")
        val goldShop: Boolean? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("owner_id")
        val ownerId: String? = null,

        @field:SerializedName("is_owner")
        val isOwner: Boolean? = null,

        @field:SerializedName("domain")
        val domain: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("location")
        val location: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("gold_shop_badge")
        val goldShopBadge: Boolean? = null,

        @field:SerializedName("uri")
        val uri: String? = null
)