package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.SerializedName
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant

data class FinishOrderParams(
        @SerializedName("order_id")
        var orderId: String = "",
        @SerializedName("user_id")
        var userId: String = "",
        @SerializedName("reason")
        var reason: String = "",
        @SerializedName("action_by")
        var actionBy: String = "buyer",
        @SerializedName("action")
        var action: String = "",
        @SerializedName("admin")
        var admin: String = "",
        @SerializedName("lang")
        var lang: String = "id",
        @SerializedName("os_type")
        var osType: String = ""
) {
    fun isValid(): Boolean {
        val numericRegex = Regex("\\d+")
        return orderId.matches(numericRegex) && orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID && userId.matches(numericRegex) && userId != "0"
    }
}