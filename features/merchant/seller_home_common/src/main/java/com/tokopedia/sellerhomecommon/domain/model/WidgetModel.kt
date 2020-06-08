package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class WidgetModel(
        @Expose
        @SerializedName("applink")
        val appLink: String?,
        @Expose
        @SerializedName("ctaText")
        val ctaText: String?,
        @Expose
        @SerializedName("dataKey")
        val dataKey: String?,
        @Expose
        @SerializedName("subtitle")
        val subtitle: String?,
        @Expose
        @SerializedName("tooltip")
        val tooltip: TooltipModel,
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("url")
        val url: String?,
        @Expose
        @SerializedName("widgetType")
        val widgetType: String?
)