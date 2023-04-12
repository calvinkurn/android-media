package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

data class GetRichListDataResponse(
    @SerializedName("fetchRichListWidgetData")
    val data: FetchRichListWidgetDataModel = FetchRichListWidgetDataModel()
)

data class FetchRichListWidgetDataModel(
    @SerializedName("data")
    val widgetData: List<RichListWidgetDataModel> = emptyList()
)

data class RichListWidgetDataModel(
    @SerializedName("dataKey")
    val dataKey: String = "",
    @SerializedName("updateInfoUnix")
    val updateInfoUnix: Long = 0L,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("headerType")
    val headerType: String = "",
    @SerializedName("sections")
    val sections: List<RichListSectionModel> = emptyList(),
    @SerializedName("errorMsg")
    val errorMsg: String = "",
    @SerializedName("showWidget")
    val shouldShowWidget: Boolean = true
)

data class RichListSectionModel(
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("caption")
    val caption: String = "",
    @SerializedName("caption")
    val items: List<RichListSectionItemsModel> = emptyList(),
)

data class RichListSectionItemsModel(
    @SerializedName("itemID")
    val itemID: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("stateText")
    val stateText: String = "",
    @SerializedName("applink")
    val appLink: String = "",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("rankTrend")
    val rankTrend: Int = 0,
    @SerializedName("rankValue")
    val rankValue: String = "0",
    @SerializedName("rankNote")
    val rankNote: String = "",
)