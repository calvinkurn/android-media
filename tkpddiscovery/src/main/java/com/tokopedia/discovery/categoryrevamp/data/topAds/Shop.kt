package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.categoryrevamp.data.productModel.BadgesItem

data class Shop(

        @field:SerializedName("badges")
        val badges: List<BadgesItem> = listOf(),

        @field:SerializedName("lucky_shop")
        val luckyShop: String = "",

        @field:SerializedName("gold_shop")
        val goldShop: Boolean = false,

        @field:SerializedName("city")
        val city: String = "",

        @field:SerializedName("owner_id")
        val ownerId: String = "",

        @field:SerializedName("is_owner")
        val isOwner: Boolean = false,

        @field:SerializedName("domain")
        val domain: String = "",

        @field:SerializedName("name")
        val name: String = "",

        @field:SerializedName("location")
        val location: String = "",

        @field:SerializedName("id")
        val id: String = "",

        @field:SerializedName("gold_shop_badge")
        val goldShopBadge: Boolean = false,

        @field:SerializedName("uri")
        val uri: String = ""
)