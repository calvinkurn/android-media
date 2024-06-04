package com.tokopedia.gm.common.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 5/3/24.
 */

data class GetElementBenefitByKeyBulkResponse(
    @SerializedName("getElementBenefitByElementKeyBulk")
    val getElementBenefitByElementKeyBulk: GetElementBenefitByKeyBulkData = GetElementBenefitByKeyBulkData()
)

data class GetElementBenefitByKeyBulkData(
    @SerializedName("result")
    val result: List<BenefitKeyValueModel> = emptyList()
) {
    fun isGrantedByElementKey(elementKey: String): Boolean {
        return result.firstOrNull { it.elementKey == elementKey }?.value == 1
    }
}

data class BenefitKeyValueModel(
    @SerializedName("elementKey")
    val elementKey: String = "",
    @SerializedName("value")
    val value: Int = 0
)
