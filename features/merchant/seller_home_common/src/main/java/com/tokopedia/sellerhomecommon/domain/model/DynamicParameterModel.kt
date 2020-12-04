package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomecommon.common.const.DateFilterType

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
        val dateType: String = DateFilterType.DATE_TYPE_WEEK,
        @Expose
        @SerializedName("post_filter")
        val postFilter: String = ""
) {

    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}