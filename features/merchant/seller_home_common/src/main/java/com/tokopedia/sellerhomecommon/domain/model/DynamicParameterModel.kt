package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomecommon.common.const.DateFilterType

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class DynamicParameterModel(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("page_source")
    val pageSource: String = "",
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("date_type")
    val dateType: String = DateFilterType.DATE_TYPE_WEEK,
    @SerializedName("post_filter")
    val postFilter: String = "",
    @SerializedName("table_filter")
    val tableFilter: String = "",
    @SerializedName("sub_page_source")
    val subPageSource: String = ""
) {

    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}