package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class FetchMultiComponentResponse(
    @SerializedName("fetchMultiComponentWidgetData")
    val fetchMultiComponentWidget: FetchMultiComponentDataResponse = FetchMultiComponentDataResponse()
)

data class FetchMultiComponentDataResponse(
    @SerializedName("data")
    val data: List<GetMultiComponentWidgetData> = listOf()
)

data class GetMultiComponentWidgetData(
    @SerializedName("dataKey")
    val dataKey: String = String.EMPTY,
    @SerializedName("tabs")
    val tabs: List<GetMultiComponentTab> = listOf(),
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("errorMsg")
    val errorMessage: String = String.EMPTY,
    @SerializedName("showWidget")
    val showWidget: Boolean = true
)

data class GetMultiComponentTab(
    @SerializedName("id")
    val id: String = String.EMPTY,
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("ticker")
    val ticker: String = String.EMPTY,
    @SerializedName("components")
    val components: List<MultiComponent> = listOf()
)

data class MultiComponent(
    @SerializedName("componentType")
    val componentType: String = String.EMPTY,
    @SerializedName("dataKey")
    val dataKey: String = String.EMPTY,
    @SerializedName("configuration")
    val configuration: String = String.EMPTY,
    @SerializedName("metricsParam")
    val metricsParam: String = String.EMPTY

)
