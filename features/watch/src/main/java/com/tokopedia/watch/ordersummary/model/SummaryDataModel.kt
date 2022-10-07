package com.tokopedia.watch.ordersummary.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SummaryDataModel(
    @Expose
    @SerializedName("fetchCardWidgetData")
    val data: Data = Data()
) {
    data class Data(
        @Expose
        @SerializedName("data")
        val list: kotlin.collections.List<List> = listOf()
    )

    data class List(
        @Expose
        @SerializedName("dataKey")
        val dataKey: String = "",
        @Expose
        @SerializedName("description")
        val description: String = "",
        @Expose
        @SerializedName("value")
        val value: String = ""
    )
}