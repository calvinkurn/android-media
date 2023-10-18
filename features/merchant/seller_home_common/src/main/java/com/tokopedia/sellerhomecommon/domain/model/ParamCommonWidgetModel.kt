package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomecommon.common.const.DateFilterType

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class ParamCommonWidgetModel(
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("page_source")
    val pageSource: String = "",
    @SerializedName("date_type")
    val dateType: String = DateFilterType.DATE_TYPE_WEEK,
) {

    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}