package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TopAdslistShop(
    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("domain")
    @Expose
    val domain: String = "",

    @SerializedName("tagline")
    @Expose
    val tagline: String = "",

    @SerializedName("location")
    @Expose
    val location: String = "",

    @SerializedName("city")
    @Expose
    val city: String = "",

    @SerializedName("image_product")
    @Expose
    val imageProduct: List<TopAdslistShopProduct> = emptyList(),

    @SerializedName("image_shop")
    @Expose
    val imageShop: TopAdslistImageShop = TopAdslistImageShop(),

    @SerializedName("gold_shop")
    @Expose
    val goldShop: Boolean = false,

    @SerializedName("lucky_shop")
    @Expose
    val luckyShop: String = "",

    @SerializedName("shop_is_official")
    @Expose
    val shopIsOfficial: Boolean = false,

    @SerializedName("owner_id")
    @Expose
    val ownerId: String = "",

    @SerializedName("is_owner")
    @Expose
    val isOwner: Boolean = false,

    @SerializedName("badges")
    @Expose
    val badges: List<TopAdsBadge> = emptyList(),

    @SerializedName("uri")
    @Expose
    val uri: String = "",

    @SerializedName("gold_shop_badge")
    @Expose
    val goldShopBadge: Boolean = false
)
