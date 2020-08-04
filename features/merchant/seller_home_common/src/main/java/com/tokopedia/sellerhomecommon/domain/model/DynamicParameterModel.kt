package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class DynamicParameterModel(
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
        val limit: Int = 0,
        @Expose
        @SerializedName("date_type")
        val dateType: String = DATE_TYPE_DAILY
) {

    companion object {
        const val DATE_TYPE_DAILY = "daily"
        const val DATE_TYPE_MONTHLY = "month"
    }

    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}