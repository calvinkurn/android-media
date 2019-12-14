package com.tokopedia.notifcenter.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 11/04/19
 */
data class NotificationUpdateItem(

        @SerializedName("notif_id")
        @Expose
        var notifId: String = "",
        @SerializedName("user_id")
        @Expose
        var userId: String = "",
        @SerializedName("shop_id")
        @Expose
        var shopId: String = "",
        @SerializedName("section_id")
        @Expose
        var sectionId: String = "",
        @SerializedName("section_icon")
        @Expose
        var sectionIcon: String? = "",
        @SerializedName("section_key")
        @Expose
        var sectionKey: String = "",
        @SerializedName("subsection_key")
        @Expose
        var subsectionKey: String = "",
        @SerializedName("template_key")
        @Expose
        var templateKey: String = "",
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("short_description")
        @Expose
        var shortDescription: String = "",
        @SerializedName("content")
        @Expose
        var content: String = "",
        @SerializedName("type_of_user")
        @Expose
        var typeOfUser: Int = 0,
        @SerializedName("create_time")
        @Expose
        var createTime: String = "",
        @SerializedName("create_time_unix")
        @Expose
        var createTimeUnix: Int = 0,
        @SerializedName("update_time")
        @Expose
        var updateTime: String = "",
        @SerializedName("button_text")
        @Expose
        var btnText: String = "",
        @SerializedName("update_time_unix")
        @Expose
        var updateTimeUnix: Long = 0,
        @SerializedName("status")
        @Expose
        var status: Long = 0,
        @SerializedName("read_status")
        @Expose
        var readStatus: Long = 0,
        @SerializedName("type_link")
        @Expose
        var typeLink: Int = 0,
        @SerializedName("data_notification")
        @Expose
        var dataNotification: DataNotification,
        @SerializedName("product_data")
        @Expose
        var productData: List<ProductData>,
        @SerializedName("total_product")
        @Expose
        var totalProducts: Int = 0
)

data class ProductData(
        @Expose
        @SerializedName("product_id")
        val productId: String = "",
        @Expose
        @SerializedName("name")
        val name: String = "",
        @Expose
        @SerializedName("url")
        val url: String = "",
        @Expose
        @SerializedName("image_url")
        val imageUrl: String = "",
        @Expose
        @SerializedName("price")
        val price: String = "0",
        @Expose
        @SerializedName("price_fmt")
        val priceFormat: String = "0",
        @Expose
        @SerializedName("currency")
        val currency: String = "",
        @Expose
        @SerializedName("prrice_idr")
        val priceIdr: String = "0",
        @Expose
        @SerializedName("is_buyable")
        val isBuyable: Boolean = false,
        @Expose
        @SerializedName("is_topads")
        val isTopAds: Boolean = false,
        @Expose
        @SerializedName("is_wishlist")
        val isWishlist: Boolean = false,
        @Expose
        @SerializedName("rating")
        val rating: String = "0",
        @Expose
        @SerializedName("count_review")
        val countReview: String = "0",
        @Expose
        @SerializedName("labels")
        val labels: List<Label> = emptyList(),
        @Expose
        @SerializedName("campaign")
        val campaign: Campaign?,
        @Expose
        @SerializedName("variant")
        val variant: List<Variant> = emptyList(),
        @Expose
        @SerializedName("shop")
        val shop: Shop?
)

data class Label (
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("color")
        val color: String = ""
)

data class Campaign(
        @SerializedName("active")
        val active: Boolean = false,
        @SerializedName("original_price")
        val originalPrice: Int = 0,
        @SerializedName("original_price_fmt")
        val originalPriceFormat: String = "",
        @SerializedName("discount_percentage")
        val discountPercentage: Int = 0,
        @SerializedName("discount_price")
        val discountPrice: Int = 0,
        @SerializedName("discount_price_fmt")
        val discountPriceFormat: String = ""
)

data class Variant (
        @Expose
        @SerializedName("value")
        val value: String = "",
        @Expose
        @SerializedName("identifier")
        val identifier: String = "",
        @Expose
        @SerializedName("hex")
        val hex: String = ""
)

data class Shop(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("location")
        val location: String = "",
        @SerializedName("badges")
        val badges: List<ShopBadge> = emptyList()
)

data class ShopBadge(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("image_url")
        val imageUrl: String = ""
)