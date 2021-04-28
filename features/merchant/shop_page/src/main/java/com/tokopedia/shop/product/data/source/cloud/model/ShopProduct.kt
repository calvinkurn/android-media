package com.tokopedia.shop.product.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopProduct {
    @SerializedName("shop_lucky")
    @Expose
    var shopLucky: Long = 0

    @SerializedName("shop_gold_status")
    @Expose
    var shopGoldStatus: Long = 0

    @SerializedName("shop_id")
    @Expose
    var shopId: String = ""

    @SerializedName("badges")
    @Expose
    var badges: List<ShopProductBadge>? = null

    @SerializedName("labels")
    @Expose
    var labels: List<ShopProductLabel>? = null

    @SerializedName("product_talk_count")
    @Expose
    var productTalkCount: String? = null

    @SerializedName("product_price")
    @Expose
    var productPrice: String? = null

    @SerializedName("product_wholesale")
    @Expose
    var productWholesale: String? = null

    @SerializedName("product_image_300")
    @Expose
    var productImage300: String? = null

    @SerializedName("product_image_700")
    @Expose
    var productImage700: String? = null

    @SerializedName("product_url")
    @Expose
    var productUrl: String? = null

    @SerializedName("shop_url")
    @Expose
    var shopUrl: String? = null

    @SerializedName("product_id")
    @Expose
    var productId: String? = null

    @SerializedName("product_image")
    @Expose
    var productImage: String? = null

    @SerializedName("product_preorder")
    @Expose
    var productPreorder: String? = null

    @SerializedName("shop_location")
    @Expose
    var shopLocation: String? = null

    @SerializedName("product_review_count")
    @Expose
    var productReviewCount: String? = null

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("rating")
    @Expose
    var rating = 0.0

    @SerializedName("sold_out_status")
    @Expose
    var isSoldOutStatus = false
}