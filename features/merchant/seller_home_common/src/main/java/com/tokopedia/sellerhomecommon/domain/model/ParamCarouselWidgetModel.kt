package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 28/03/23.
 */

data class ParamCarouselWidgetModel(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("limit")
    val limit: Int = 0
) {
    fun toJsonString(): String {
        return Gson().toJson(this)
    }
}