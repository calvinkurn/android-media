package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName


data class DataItem(

        @SerializedName("chipSelectionType")
        var chipSelectionType: String = "0",

        @SerializedName("key")
        val key: String? = "",

        @SerializedName("target_component")
        val targetComponent: String? = "",

        @SerializedName("value")
        val value: String? = "",

        @SerializedName("target_component_id")
        val targetComponentId: String? = "",

        @SerializedName("background_image")
        val backgroundImage: String? = "",

        @SerializedName("filter")
        val filter: String? = "",

        @SerializedName("filter_value")
        val filterValue: String? = "",

        @SerializedName("isSelected")
        var isSelected: Boolean = false,

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

        @SerializedName("name", alternate = ["text"])
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

        @SerializedName("box_color", alternate = ["background_color"])
        val boxColor: String? = "",

        @SerializedName("font_color", alternate = ["text_color"])
        val fontColor: String? = "",

        @field:SerializedName("button_text")
        var buttonText: String? = "",

        @field:SerializedName("creative_name")
        var creativeName: String? = "",

        @SerializedName("title")
        val title: String? = "",

        @SerializedName("thumbnail_url_mobile", alternate = ["imageURL", "icon_url"])
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

        @field:SerializedName("image_click_url", alternate = ["url"])
        var imageClickUrl: String? = "",

        @SerializedName("size_mobile")
        var sizeMobile: String? = "",

        @SerializedName("background")
        var background: String? = "",

        @SerializedName("video_id")
        val videoId: String? = "",

        @SerializedName("category_rows")
        val categoryRows: List<DataItem>? = ArrayList(),

        @SerializedName("type")
        val type: String = "",

        @SerializedName("categoryLabel")
        val categoryLabel: String = "",

        @SerializedName("ID")
        val id: String? = "",

        @SerializedName("shop_id")
        val shopId: String? = "",

        @SerializedName("ongoing_campaign_start_time")
        val ongoingCampaignStartTime: String? = "",

        @SerializedName("ongoing_campaign_end_time")
        val ongoingCampaignEndTime: String? = "",

        @SerializedName("upcoming_campaign_start_time")
        val upcomingCampaignStartTime: String? = "",

        @SerializedName("upcoming_campaign_end_time")
        val upcomingCampaignEndTime: String? = "",

        @SerializedName("timer_font_color")
        val timerFontColor: String? = "",

        @SerializedName("timer_box_color")
        val timerBoxColor: String? = ""

) {
    val leftMargin: Int
        get() {
            return leftMarginMobile?.toIntOrNull() ?: 0
        }

    val rightMargin: Int
        get() {
            return rightMarginMobile?.toIntOrNull() ?: 0
        }
}