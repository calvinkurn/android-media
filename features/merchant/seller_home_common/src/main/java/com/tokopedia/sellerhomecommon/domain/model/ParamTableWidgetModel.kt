package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 29/03/23.
 */

data class ParamTableWidgetModel(
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("page_source")
    val pageSource: String = "",
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("table_filter")
    val tableFilter: String = "",
    @SerializedName("sub_page_source")
    val subPageSource: String = ""
) {
    fun toJsonString(): String = Gson().toJson(this)
}