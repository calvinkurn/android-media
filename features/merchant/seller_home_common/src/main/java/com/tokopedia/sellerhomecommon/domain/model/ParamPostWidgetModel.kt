package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 29/03/23.
 */

data class ParamPostWidgetModel(
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("post_filter")
    val postFilter: String = "",
) {
    fun toJsonString(): String = Gson().toJson(this)
}