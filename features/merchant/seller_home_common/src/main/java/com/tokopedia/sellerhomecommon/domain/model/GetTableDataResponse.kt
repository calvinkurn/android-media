package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class GetTableDataResponse(
    @SerializedName("fetchSearchTableWidgetData")
    val fetchSearchTableWidgetData: FetchSearchTableWidgetDataModel = FetchSearchTableWidgetDataModel()
)

data class FetchSearchTableWidgetDataModel(
    @SerializedName("data")
    val `data`: List<TableDataModel> = emptyList()
)

data class TableDataModel(
    @SerializedName("data")
    val `data`: TableDataSetModel = TableDataSetModel(),
    @SerializedName("dataKey")
    val dataKey: String = "",
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("errorMsg")
    val errorMsg: String = "",
    @SerializedName("showWidget")
    val showWidget: Boolean = false
)

data class TableDataSetModel(
    @SerializedName("headers")
    val headers: List<HeaderModel> = emptyList(),
    @SerializedName("rows")
    val rows: List<RowModel> = emptyList()
)

data class HeaderModel(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("width")
    val width: Int = 0
)

data class RowModel(
    @SerializedName("columns")
    val columns: List<ColumnModel> = emptyList(),
    @SerializedName("id")
    val id: String = ""
)

data class ColumnModel(
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("value")
    val value: String = "",
    @SerializedName("iconUrl")
    val iconUrl: String? = "",
    @SerializedName("meta")
    val meta: String = ""
)

data class TableRowMeta(
    @SerializedName("flag")
    val flag: String = String.EMPTY
)
