package com.tokopedia.home.beranda.domain.model.recharge_bu_widget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargePerso(
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("media_url")
        val mediaUrl: String = "",
        @Expose
        @SerializedName("app_link")
        val applink: String = "",
        @Expose
        @SerializedName("web_link")
        val weblink: String = "",
        @Expose
        @SerializedName("text_link")
        val textlink: String = "",
        @Expose
        @SerializedName("option_1")
        val option1: String = "mix_left",
        @Expose
        @SerializedName("option_2")
        val option2: String = "",
        @Expose
        @SerializedName("option_3")
        val option3: String = "",
        @Expose
        @SerializedName("tracking")
        val tracking: List<RechargeBUWidgetTracking> = listOf(),
        @Expose
        @SerializedName("items")
        val items: List<RechargePersoItem> = listOf()
) {
    data class Response(
            @SerializedName("getBUWidget")
            val response: RechargePerso? = RechargePerso()
    )
}