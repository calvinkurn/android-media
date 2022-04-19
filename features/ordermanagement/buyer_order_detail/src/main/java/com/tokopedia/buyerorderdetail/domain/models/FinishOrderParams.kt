package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant

data class FinishOrderParams(
    @Expose
    @SerializedName("order_id")
    var orderId: String = "",
    @Expose
    @SerializedName("user_id")
    var userId: String = "",
    @Expose
    @SerializedName("reason")
    var reason: String = "",
    @Expose
    @SerializedName("action_by")
    var actionBy: String = "buyer",
    @Expose
    @SerializedName("action")
    var action: String = "",
    @Expose
    @SerializedName("admin")
    var admin: String = "",
    @Expose
    @SerializedName("lang")
    var lang: String = "id",
    @Expose
    @SerializedName("os_type")
    var osType: String = ""
) {
    fun isValid(): Boolean {
        val numericRegex = Regex("\\d+")
        return orderId.matches(numericRegex) && orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID && userId.matches(
            numericRegex
        ) && userId != "0"
    }
}