package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.mapper.MiniCartSimplifiedMapper

data class MiniCartData(
    @SerializedName("data")
    val data: Data = Data(),
    @SerializedName("error_message")
    val errorMessage: List<String> = emptyList(),
    @SerializedName("status")
    val status: String = ""
) {

    fun toSimplifiedData(): MiniCartSimplifiedData {
        return MiniCartSimplifiedMapper.mapMiniCartSimplifiedData(this)
    }
}
