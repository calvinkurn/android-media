package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.constant.LogisticConstant.BO_TYPE_PLUS
import com.tokopedia.purchase_platform.common.constant.LogisticConstant.BO_TYPE_PLUS_DT

data class FreeShippingGeneral(
        @SerializedName("badge_url")
        val badgeUrl: String = "",
        @SerializedName("bo_type")
        val boType: Int = 0,
        @SerializedName("bo_name")
        val boName: String = "",
) {

    fun isBoTypePlus(): Boolean {
        return boType == BO_TYPE_PLUS || boType == BO_TYPE_PLUS_DT
    }
}
