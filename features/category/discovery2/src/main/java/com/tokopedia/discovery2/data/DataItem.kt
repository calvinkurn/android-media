package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName


data class DataItem(

        @SerializedName("end_date")
        val endDate: String? = "",

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
        val imageUrlMobile: String? = "",

        @SerializedName("image_url_dynamic_mobile")
        val imageUrlDynamicMobile: String? = "",

        @SerializedName("applinks", alternate = ["applink"])
        val applinks: String? = "",

        @SerializedName("name")
        val name: String? = "",

        @SerializedName("action")
        val action: String? = "",

        @SerializedName("notification_title")
        val notificationTitle: String? = "",

        @SerializedName("notification_description")
        val notificationDescription: String? = "",

        @SerializedName("start_date")
        val startDate: String? = "",

        @SerializedName("left_margin_mobile")
        val leftMarginMobile: String? = "0",

        @SerializedName("right_margin_mobile")
        val rightMarginMobile: String? = "0",

        @SerializedName("background_url_mobile")
        val backgroundUrlMobile: String? = "",

        @SerializedName("alternate_background_url_mobile")
        val alternateBackgroundUrlMobile: String? = "",

        @SerializedName("box_color")
        val boxColor: String? = "",

        @SerializedName("font_color")
        val fontColor: String? = "",

        @SerializedName("button_text")
        val buttonText: String? = "",

        @SerializedName("btn_applink")
        val btnApplink: String? = "",

        @SerializedName("title")
        val title: String? = ""

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