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
    val widgetName: String = "",
    @SerializedName("header")
    @Expose
    val headerInput: HeaderInput = HeaderInput()
) {
    data class HeaderInput(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",
        @SerializedName("ctaText")
        @Expose
        val ctaText: String = "",
        @SerializedName("ctaLink")
        @Expose
        val ctaLink: String = "",
        @SerializedName("isATC")
        @Expose
        val isAtc: Int = -1,
        @SerializedName("etalaseID")
        @Expose
        val etalaseId: String = "",
        @SerializedName("isShowEtalaseName")
        @Expose
        val isShowEtalaseName: Int = -1,
        @SerializedName("data")
        @Expose
        val listData: List<DataOnHeaderInput> = listOf(),
    ) {
        data class DataOnHeaderInput(
            @SerializedName("linkID")
            @Expose
            val linkId: String = "",
            @SerializedName("linkType")
            @Expose
            val linkType: String = ""
        )
    }
}
