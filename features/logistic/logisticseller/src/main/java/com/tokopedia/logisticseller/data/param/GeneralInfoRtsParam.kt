package com.tokopedia.logisticseller.data.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GeneralInfoRtsParam(
    @SerializedName("input")
    val input: GeneralInfoRtsInput = GeneralInfoRtsInput()
) : GqlParam {

    data class GeneralInfoRtsInput(
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("action")
        val action: String = "",
    ) : GqlParam

    companion object {
        const val ACTION_RTS_CONFIRMATION = "rts_confirmation"
        const val ACTION_RTS_HELPER = "rts_helper"
    }
}
