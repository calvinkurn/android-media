package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPageWidgetRequestModel(
    @SerializedName("widgetID")
    @Expose
    val widgetID: String = "",
    @SerializedName("widgetMasterID")
    @Expose
    val widgetMasterID: String = "",
    @SerializedName("widgetType")
    @Expose
    val widgetType: String = "",
    @SerializedName("widgetName")
    @Expose
    val widgetName: String = ""
)
