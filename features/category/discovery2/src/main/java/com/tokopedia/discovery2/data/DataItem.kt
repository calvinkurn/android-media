package com.tokopedia.discovery2.data

import android.media.Rating
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.data.productcardcarousel.FreeOngkir


data class DataItem(

        @SerializedName("end_date")
        val endDate: String? = "",

        @SerializedName("button_applink")
        val buttonApplink: String? = "",

        @SerializedName("code")
        val code: String? = "",

        @SerializedName("mobile_url")
        val mobileUrl: String? = "",

        @SerializedName("image_alt")
        val imageAlt: String? = "",

        @SerializedName("params_mobile")
        val paramsMobile: String? = "",

        @SerializedName("notification_id")
        val notificationId: String? = "",

        @SerializedName("image_title")
        val imageTitle: String? = "",

        @SerializedName("registered_image_app")
        val registeredImageApp: String? = "",

        @SerializedName("image_url_mobile")
        var imageUrlMobile: String? = "",

        @SerializedName("image_url_dynamic_mobile")
        val imageUrlDynamicMobile: String? = "",

        @SerializedName("applinks", alternate = ["applink"])
        var applinks: String? = "",

        @SerializedName("name")
        var name: String? = "",

        @SerializedName("action")
        val action: String? = "",

        @SerializedName("notification_title")
        val notificationTitle: String? = "",

        @SerializedName("notification_description")
        val notificationDescription: String? = "",

        @SerializedName("description")
        val description: String? = "",

        @SerializedName("start_date")
        val startDate: String? = "",

        @SerializedName("left_margin_mobile")
        val leftMarginMobile: String? = "0",

        @SerializedName("right_margin_mobile")
        val rightMarginMobile: String? = "0",

        @SerializedName("category_detail_url")
        val categoryDetailUrl: String? = "",

        @SerializedName("background_image_apps")
        val backgroundImageApps: String? = "",

        @SerializedName("background_url_mobile")
        val backgroundUrlMobile: String? = "",

        @SerializedName("alternate_background_url_mobile")
        val alternateBackgroundUrlMobile: String? = "",

        @SerializedName("box_color")
        val boxColor: String? = "",

        @SerializedName("font_color")
        val fontColor: String? = "",

        @field:SerializedName("button_text")
        var buttonText: String? = "",

        @SerializedName("title")
        val title: String? = "",

        @SerializedName("thumbnail_url_mobile")
        val thumbnailUrlMobile: String? = "",

        @SerializedName("points_str")
        val pointsStr: String? = "",

        @SerializedName("points_slash_str")
        val pointsSlashStr: String? = "",

        @SerializedName("discount_percentage_str")
        val discountPercentageStr: String? = "",

        @SerializedName("points_slash")
        val pointsSlash: String? = "",

        @SerializedName("slug")
        val slug: String? = "",

        @SerializedName("btn_applink")
        val btnApplink: String? = "",

        @field:SerializedName("price_format")
        var priceFormat: String? = "",

        @field:SerializedName("image_click_url")
        var imageClickUrl: String? = "",

        @SerializedName("size_mobile")
        var sizeMobile: String? = "",

        @SerializedName("background")
        var background: String? = "",

        @SerializedName("video_id")
        val videoId: String? = "",

        @SerializedName("cashback")
        val cashback: String? = "",

        @SerializedName("is_topads")
        val isTopads: String? = "",

        @SerializedName("discounted_price")
        val discountedPrice: String? = "",

        @SerializedName("gold_merchant")
        val goldMerchant: Boolean? = false,

        @SerializedName("price")
        val price: String? = "",

        @SerializedName("shop_name")
        val shopName: String? = "",

        @SerializedName("shop_location")
        val shopLocation: String? = "",

        @SerializedName("discount_percentage")
        val discountPercentage: String? = "",

        @SerializedName("shop_applink")
        val shopApplink: String? = "",

        @SerializedName("count_review")
        val countReview: String? = "",

        @SerializedName("preorder")
        val preorder: String? = "",

        @SerializedName("topads_view_url")
        val topadsViewUrl: String? = "",

        @SerializedName("product_id")
        val productId: String? = "",

        @SerializedName("rating")
        val rating: String? = "",

        @SerializedName("shop_logo")
        val shopLogo: String? = "",

        @SerializedName("official_store")
        val officialStore: Boolean? = false,

        @SerializedName("stock")
        val stock: String? = "",

        @SerializedName("stock_sold_percentage")
        val stockSoldPercentage: String? = "",

        @SerializedName("topads_click_url")
        val topadsClickUrl: String? = "",

        @SerializedName("free_ongkir")
        val freeOngkir: Any? = ""




        ) {
    val leftMargin: Int
        get() {
            if (leftMarginMobile != null && !leftMarginMobile.isEmpty()) leftMarginMobile.toInt()
            return 0
        }

    val rightMargin: Int
        get() {
            if (rightMarginMobile != null && !rightMarginMobile.isEmpty()) rightMarginMobile.toInt()
            return 0
        }
}