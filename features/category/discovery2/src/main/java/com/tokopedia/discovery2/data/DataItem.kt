package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName


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

        @SerializedName("points_str")
        val pointsStr: String? = "",

        @SerializedName("points_slash_str")
        val pointsSlashStr: String? = "",

        @SerializedName("discount_percentage_str")
        val discountPercentageStr: String? = "",

        @SerializedName("points_slash")
        val pointsSlash: Int? = 0,

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

        @SerializedName("basecode")
        val basecode: String? = "",
        @SerializedName("catalog_type")
        val catalogType: Int? = 0,
        @SerializedName("coupon_code")
        val couponCode: String? = "",
        @SerializedName("cta")
        val cta: String? = "",
        @SerializedName("cta_desktop")
        val ctaDesktop: String? = "",
        @SerializedName("disabled_err_msg")
        val disabledErrMsg: String? = "",
        @SerializedName("id")
        val id: Int? = 0,
        @SerializedName("image_url")
        val imageUrl: String? = "",
        @SerializedName("is_disabled")
        val isDisabled: Boolean? = false,
        @SerializedName("is_disabled_btn")
        val isDisabledBtn: Boolean? = false,
        @SerializedName("min_usage")
        val minUsage: String? = "",
        @SerializedName("min_usage_label")
        val minUsageLabel: String? = "",
        @SerializedName("promo_id")
        val promoId: Int? = 0,
        @SerializedName("quota")
        val quota: Int? = 0,
        @SerializedName("slug")
        val slug: String? = "",
        @SerializedName("subtitle")
        val subtitle: String? = "",
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String? = "",
        @SerializedName("thumbnail_url_mobile")
        val thumbnailUrlMobile: String? = "",
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("upper_text_desc")
        val upperTextDesc: List<String?>? = null

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