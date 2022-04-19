package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetTableDataResponse(
        @Expose
        @SerializedName("fetchSearchTableWidgetData")
        val fetchSearchTableWidgetData: FetchSearchTableWidgetDataModel = FetchSearchTableWidgetDataModel()
)

data class FetchSearchTableWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: List<TableDataModel> = emptyList()
)

data class TableDataModel(
        @Expose
        @SerializedName("data")
        val `data`: TableDataSetModel = TableDataSetModel(),
        @Expose
        @SerializedName("dataKey")
        val dataKey: String = "",
        @Expose
        @SerializedName("error")
        val error: Boolean = false,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String = "",
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean = false
)

data class TableDataSetModel(
        @Expose
        @SerializedName("headers")
        val headers: List<HeaderModel> = emptyList(),
        @Expose
        @SerializedName("rows")
        val rows: List<RowModel> = emptyList()
)

data class HeaderModel(
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("width")
        val width: Int = 0
)

data class RowModel(
        @Expose
        @SerializedName("columns")
        val columns: List<ColumnModel> = emptyList(),
        @Expose
        @SerializedName("id")
        val id: String = ""
)

data class ColumnModel(
        @Expose
        @SerializedName("type")
        val type: Int = 0,
        @Expose
        @SerializedName("value")
        val value: String = ""
)