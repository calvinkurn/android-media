package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 23/11/22.
 */

class DynamicSellerStateInfoParamModel(
    @SerializedName("shop_id")
    val shopId: String = String.EMPTY,
    @SerializedName("page_source")
    val pageSource: String = String.EMPTY,
) {
    fun toJsonString(): String {
        return Gson().toJsonTree(this).toString()
    }
}