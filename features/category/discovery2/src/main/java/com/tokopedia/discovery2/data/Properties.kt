package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID

data class Properties(
        @SerializedName("columns")
        val columns: String?,

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
        val design: String,

        @SerializedName("type")
        val type: String,

        @SerializedName("limit_number")
        val limitNumber: String = "20",

        @SerializedName("limit_product")
        val limitProduct: Boolean,

        @SerializedName("target_id")
        val targetId: String?,

        @SerializedName("template")
        val template: String = GRID

)