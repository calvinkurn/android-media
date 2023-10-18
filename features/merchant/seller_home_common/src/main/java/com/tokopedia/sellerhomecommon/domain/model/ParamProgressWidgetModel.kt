package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 29/03/23.
 */

data class ParamProgressWidgetModel(
    @SerializedName("date")
    val date: String = ""
) {
    fun toJsonString(): String = Gson().toJson(this)
}