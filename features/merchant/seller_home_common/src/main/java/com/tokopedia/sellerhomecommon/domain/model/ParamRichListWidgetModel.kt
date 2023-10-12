package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 11/06/20
 */

data class ParamRichListWidgetModel(
    @SerializedName("shop_id")
    private val shopId: String = "",
    @SerializedName("page_source")
    private val pageSource: String = ""
) {

    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}