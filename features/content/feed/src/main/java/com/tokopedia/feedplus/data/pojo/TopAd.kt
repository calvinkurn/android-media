package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class TopAd(
    @SerializedName("id")
    @Expose
    val id: String = "",

    @SerializedName("ad_ref_key")
    @Expose val adRefKey: String = "",

    @SerializedName("redirect")
    @Expose
    val redirect: String = "",

    @SerializedName("sticker_id")
    @Expose
    val stickerId: String = "",

    @SerializedName("sticker_image")
    @Expose
    val stickerImage: String = "",

    @SerializedName("product_click_url")
    @Expose
    val productClickUrl: String = "",

    @SerializedName("shop_click_url")
    @Expose
    val shopClickUrl: String = "",

    @SerializedName("product")
    @Expose
    val product: TopAdslistProduct = TopAdslistProduct(),

    @SerializedName("shop")
    @Expose
    val shop: TopAdslistShop = TopAdslistShop(),

    @SerializedName("applinks")
    @Expose
    val applinks: String = ""
)
