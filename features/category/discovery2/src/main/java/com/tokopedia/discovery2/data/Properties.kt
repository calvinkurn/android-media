package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class Properties(
        @SerializedName("columns")
        val columns: String? = null,

        @SerializedName("button_notification")
        val buttonNotification: Boolean? = null,

        @SerializedName("registered_message")
        val registeredMessage: String? = null,

        @SerializedName("unregistered_message")
        val unregisteredMessage: String? = null,

        @SerializedName("background")
        val background: String? = null,

        @SerializedName("dynamic")
        val dynamic: Boolean = false,

        @SerializedName("sticky")
        val sticky: Boolean = false,

        @SerializedName("banner_title")
        val bannerTitle: String? = null,

        @SerializedName("cta_app")
        val ctaApp: String? = null,

        @SerializedName("design")
        val design: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("limit_number")
        val limitNumber: String = "20",

        @SerializedName("limit_product")
        val limitProduct: Boolean = false,

        @SerializedName("target_id")
        var targetId: String? = null

)