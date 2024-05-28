package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.SerializedName

data class WidgetIdList(
    @SerializedName("widgetID")
    val widgetId: String = "",

    @SerializedName("widgetMasterID")
    val widgetMasterId: String = "",

    @SerializedName("widgetType")
    val widgetType: String = "",

    @SerializedName("widgetName")
    val widgetName: String = "",

    @SerializedName("isFestivity")
    val isFestivity: Boolean = false,

    @SerializedName("header")
    val header: Header = Header(),

    @SerializedName("options")
    val options: List<Options> = emptyList()
)
