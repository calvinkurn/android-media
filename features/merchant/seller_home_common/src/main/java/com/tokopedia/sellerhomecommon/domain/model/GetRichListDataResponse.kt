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
    val type: String = "",
    @SerializedName("caption")
    val caption: String = "",
    @SerializedName("items")
    val items: List<RichListSectionItemsModel> = emptyList(),
)

data class RichListSectionItemsModel(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("stateText")
    val stateText: String = "",
    @SerializedName("rankTrend")
    val rankTrend: String = "",
    @SerializedName("rankValue")
    val rankValue: String = "0",
    @SerializedName("rankNote")
    val rankNote: String = "",
    @SerializedName("stateTooltip")
    val stateTooltip: StateTooltipModel = StateTooltipModel(),
)

data class StateTooltipModel(
    @SerializedName("show")
    val shouldShow: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = ""
)