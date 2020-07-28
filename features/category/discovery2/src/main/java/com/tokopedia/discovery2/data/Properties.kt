package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class Properties(
        @SerializedName("columns")
        val columns: String?,

        @SerializedName("button_notification")
        val buttonNotification: Boolean?,

        @SerializedName("registered_message")
        val registeredMessage: String?,

        @SerializedName("unregistered_message")
        val unregisteredMessage: String?,

        @SerializedName("background")
        val background: String?,

        @SerializedName("dynamic")
        val dynamic: Boolean,

        @SerializedName("sticky")
        val sticky: Boolean,

        @SerializedName("banner_title")
        val bannerTitle: String,

        @SerializedName("cta_app")
        val ctaApp: String?,

        @SerializedName("design")
        val design: String

)