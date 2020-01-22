package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 2020-01-21
 */

data class CardWidgetModel(
        @SerializedName("widgetType")
        override val widgetType: String,
        @SerializedName("title")
        override val title: String,
        @SerializedName("url")
        override val url: String,
        @SerializedName("applink")
        override val appLink: String
) : BaseWidgetModel