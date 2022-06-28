package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WidgetIdList(
    @SerializedName("widgetID")
    @Expose
    val widgetId: String = "",

    @SerializedName("widgetMasterID")
    @Expose
    val widgetMasterId: String = "",

    @SerializedName("widgetType")
    @Expose
    val widgetType: String = "",

    @SerializedName("widgetName")
    @Expose
    val widgetName: String = ""
)