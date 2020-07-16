package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class WidgetDataParameterModel(
        @Expose
        @SerializedName("date")
        val date: String = "",
        @Expose
        @SerializedName("start_date")
        val startDate: String = "",
        @Expose
        @SerializedName("end_date")
        val endDate: String = "",
        @Expose
        @SerializedName("page_source")
        val pageSource: String = "",
        @Expose
        @SerializedName("page")
        val page: Int = 0,
        @Expose
        @SerializedName("limit")
        val limit: Int = 0
) {

    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}