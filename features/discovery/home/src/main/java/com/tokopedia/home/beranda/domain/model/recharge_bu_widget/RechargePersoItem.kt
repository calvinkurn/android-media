package com.tokopedia.home.beranda.domain.model.recharge_bu_widget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargePersoItem(
        @Expose
        @SerializedName("id")
        val id: String = "",
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("media_url")
        val mediaUrl: String = "",
        @Expose
        @SerializedName("subtitle")
        val subtitle: String = "",
        @Expose
        @SerializedName("label_1")
        val label1: String = "",
        @Expose
        @SerializedName("label_2")
        val label2: String = "",
        @Expose
        @SerializedName("label_3")
        val label3: String = "",
        @Expose
        @SerializedName("app_link")
        val applink: String = "",
        @Expose
        @SerializedName("web_link")
        val weblink: String = "",
        @Expose
        @SerializedName("tracking")
        val tracking: List<RechargeBUWidgetTracking> = listOf()
)