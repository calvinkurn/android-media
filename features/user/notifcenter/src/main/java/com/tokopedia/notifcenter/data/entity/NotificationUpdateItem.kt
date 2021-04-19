package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifcenter.data.annotation.UsedInNewRevamp

/**
 * @author : Steven 11/04/19
 */
data class NotificationUpdateItem(
        @Expose @SerializedName("notif_id") var notifId: String = "",
        @Expose @SerializedName("user_id") var userId: String = "",
        @Expose @SerializedName("shop_id") var shopId: String = "",
        @Expose @SerializedName("section_id") var sectionId: String = "",
        @Expose @SerializedName("section_icon") var sectionIcon: String? = "",
        @Expose @SerializedName("section_key") var sectionKey: String = "",
        @Expose @SerializedName("subsection_key") var subsectionKey: String = "",
        @Expose @SerializedName("template_key") var templateKey: String = "",
        @Expose @SerializedName("title") var title: String = "",
        @Expose @SerializedName("short_description") var shortDescription: String = "",
        @Expose @SerializedName("short_description_html") var shortDescriptionHtml: String = "",
        @Expose @SerializedName("content") var content: String = "",
        @Expose @SerializedName("type_of_user") var typeOfUser: Int = 0,
        @Expose @SerializedName("create_time") var createTime: String = "",
        @Expose @SerializedName("create_time_unix") var createTimeUnix: Int = 0,
        @Expose @SerializedName("update_time") var updateTime: String = "",
        @Expose @SerializedName("button_text") var btnText: String = "",
        @Expose @SerializedName("update_time_unix") var updateTimeUnix: Long = 0,
        @Expose @SerializedName("status") var status: Long = 0,
        @Expose @SerializedName("read_status") var readStatus: Long = 0,
        @Expose @SerializedName("type_link") var typeLink: Int = 0,
        @Expose @SerializedName("data_notification") var dataNotification: DataNotification,
        @Expose @SerializedName("product_data") var productData: List<ProductData>,
        @Expose @SerializedName("total_product") var totalProducts: Int = 0,
        @Expose @SerializedName("is_longer_content") var isLongerContent: Boolean = false,
        @Expose @SerializedName("show_bottomsheet") var isShowBottomSheet: Boolean = false,
        @Expose @SerializedName("type_bottomsheet") var typeBottomSheet: Int = 0
)

data class ProductData(
        @Expose @SerializedName("product_id") val productId: String = "",
        @Expose @SerializedName("name") val name: String = "",
        @Expose @SerializedName("url") val url: String = "",
        @Expose @SerializedName("image_url") val imageUrl: String = "",
        @Expose @SerializedName("price") val price: String = "0",
        @Expose @SerializedName("price_fmt") val priceFormat: String = "0",
        @Expose @SerializedName("currency") val currency: String = "",
        @Expose @SerializedName("prrice_idr") val priceIdr: String = "0",
        @Expose @SerializedName("is_buyable") val isBuyable: Boolean = false,
        @Expose @SerializedName("is_topads") val isTopAds: Boolean = false,
        @Expose @SerializedName("is_wishlist") val isWishlist: Boolean = false,
        @Expose @SerializedName("rating") val rating: String = "0",
        @Expose @SerializedName("count_review") val countReview: String = "0",
        @Expose @SerializedName("labels") val labels: List<Label> = emptyList(),
        @Expose @SerializedName("campaign") val campaign: Campaign? = null,
        @Expose @SerializedName("variant") val variant: List<Variant> = emptyList(),
        @Expose @SerializedName("shop") val shop: Shop? = null,
        @Expose @SerializedName("stock") var stock: Int = 0,
        @Expose @SerializedName("type_button") var typeButton: Int = 0,
        @Expose @SerializedName("is_show") var isShow: Boolean = false,
        @Expose @SerializedName("has_reminder") var hasReminder: Boolean = false
)

data class Label (
        @Expose @SerializedName("title") val title: String = "",
        @Expose @SerializedName("color") val color: String = ""
)

@UsedInNewRevamp
data class Campaign(
        @SerializedName("active") val active: Boolean = false,
        @SerializedName("original_price") val originalPrice: Int = 0,
        @SerializedName("original_price_fmt") val originalPriceFormat: String = "",
        @SerializedName("discount_percentage") val discountPercentage: Int = 0,
        @SerializedName("discount_price") val discountPrice: Int = 0,
        @SerializedName("discount_price_fmt") val discountPriceFormat: String = ""
)

@UsedInNewRevamp
data class Variant (
        @Expose @SerializedName("value") val value: String = "",
        @Expose @SerializedName("identifier") val identifier: String = "",
        @Expose @SerializedName("hex") val hex: String = ""
)

data class Shop(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("name") val name: String = "",
        @SerializedName("location") val location: String = "",
        @SerializedName("badges") val badges: List<ShopBadge>? = emptyList(),
        @SerializedName("free_shipping_icon") val freeShippingIcon: String? = ""
)

data class ShopBadge(
        @SerializedName("title") val title: String = "",
        @SerializedName("image_url") val imageUrl: String = ""
)

data class NotificationOptions(
        @SerializedName("longer_content") val contentMaxLonger: Int = 0
)