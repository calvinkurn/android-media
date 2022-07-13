package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.AttachmentVariant

data class ProductProfile (
    @SerializedName("min_order")
    val minOrder: Int = 1,

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("price")
    val price: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("price_int")
    val priceInt: Double = 0.0,

    @SerializedName("category_breadcrumb")
    val category: String = "",

    @SerializedName("category_id")
    val categoryId: String = "0",

    @SerializedName("variant")
    val variant: List<AttachmentVariant>? = listOf(),

    @SerializedName("drop_percentage")
    var dropPercentage: String = "",

    @SerializedName("price_before")
    var priceBefore: String = "",

    @SerializedName("shop_id")
    val shopId: String = "0",

    @SerializedName("free_ongkir")
    val freeShipping: FreeShipping = FreeShipping(),

    @SerializedName("playstore_product_data")
    val playStoreData: PlayStoreData = PlayStoreData(),

    @SerializedName("remaining_stock")
    var remainingStock: Int = 0,

    @SerializedName("status")
    var status: Int = 1,

    @SerializedName("wishlist")
    val wishList: Boolean = false,

    @SerializedName("list_image_url")
    val images: List<String> = emptyList(),

    @SerializedName("rating")
    val rating: TopchatProductRating = TopchatProductRating(),

    @SerializedName("is_preorder")
    val isPreOrder: Boolean = false,

    @SerializedName("campaign_id")
    var campaignId: String = "0",

    @SerializedName("is_fulfillment")
    var isFulFillment: Boolean = false,

    @SerializedName("icon_tokocabang")
    var urlTokocabang: String = "",

    @SerializedName("desc_tokocabang")
    var descTokocabang: String = "",

    @SerializedName("parent_id")
    var parentId: String = "0",

    @SerializedName("is_variant")
    var isSupportVariant: Boolean = false,

    @SerializedName("is_upcoming_campaign_product")
    var isUpcomingCampaign: Boolean = false,

    @SerializedName("location_stock")
    var locationStock: LocationStock = LocationStock(),

    @SerializedName("android_url")
    var androidUrl: String = "",

    @SerializedName("ios_url")
    var iosUrl: String = ""
)
