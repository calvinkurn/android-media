package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 2020-01-21
 */

data class WidgetModel(
        @SerializedName("applink")
        val appLink: String?,
        @SerializedName("ctaText")
        val ctaText: String?,
        @SerializedName("dataKey")
        val dataKey: String?,
        @SerializedName("subtitle")
        val subtitle: String?,
        @SerializedName("tooltip")
        val tooltip: TooltipModel,
        @SerializedName("title")
        val title: String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("widgetType")
        val widgetType: String?
)