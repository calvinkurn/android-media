package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

data class GetUnificationDataResponse(
    @SerializedName("fetchNavigationTabWidgetData")
    val navigationTab: FetchUnificationDataModel = FetchUnificationDataModel()
)

data class FetchUnificationDataModel(
    @SerializedName("data")
    val data: List<UnificationDataModel> = listOf()
)

data class UnificationDataModel(
    @SerializedName("dataKey")
    val dataKey: String = "",
    @SerializedName("errorMsg")
    val errorMsg: String = "",
    @SerializedName("showWidget")
    val showWidget: Boolean = true,
    @SerializedName("tabs")
    val tabs: List<UnificationTabModel> = listOf()
)

data class UnificationTabModel(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("isNew")
    val isNew: Boolean = false,
    @SerializedName("isUnauthorized")
    val isUnauthorized: Boolean = false,
    @SerializedName("itemCount")
    val itemCount: Int = 0,
    @SerializedName("tooltip")
    val tooltip: String = "",
    @SerializedName("content")
    val content: UnificationTabContentModel = UnificationTabContentModel(),
)

data class UnificationTabContentModel(
    @SerializedName("widget_type")
    val widgetType: String = "",
    @SerializedName("datakey")
    val dataKey: String = "",
    @SerializedName("configuration")
    val configuration: String = "",
    @SerializedName("metricsParam")
    val metricsParam: String = "",
)