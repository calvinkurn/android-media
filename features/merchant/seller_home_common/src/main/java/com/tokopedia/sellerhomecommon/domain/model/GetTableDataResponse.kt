package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetTableDataResponse(
        @Expose
        @SerializedName("fetchSearchTableWidgetData")
        val fetchSearchTableWidgetData: FetchSearchTableWidgetDataModel
)

data class FetchSearchTableWidgetDataModel(
        @Expose
        @SerializedName("data")
        val `data`: List<TableDataModel>
)

data class TableDataModel(
        @Expose
        @SerializedName("data")
        val `data`: TableDataSetModel,
        @Expose
        @SerializedName("dataKey")
        val dataKey: String,
        @Expose
        @SerializedName("error")
        val error: Boolean,
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String
)

data class TableDataSetModel(
        @Expose
        @SerializedName("headers")
        val headers: List<HeaderModel>,
        @Expose
        @SerializedName("rows")
        val rows: List<RowModel>
)

data class HeaderModel(
        @Expose
        @SerializedName("title")
        val title: String,
        @Expose
        @SerializedName("width")
        val width: Int
)

data class RowModel(
        @Expose
        @SerializedName("columns")
        val columns: List<ColumnModel>,
        @Expose
        @SerializedName("id")
        val id: String
)

data class ColumnModel(
        @Expose
        @SerializedName("type")
        val type: Int,
        @Expose
        @SerializedName("value")
        val value: String
)