package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class TxStatsDynamicPdp(
        @SerializedName("transactionSuccess")
        val txSuccess: String = "",
        @SerializedName("transactionReject")
        val txReject: String = "",
        @SerializedName("countSold")
        val countSold: String = "",
        @SerializedName("paymentVerified")
        val paymentVerified: String = "",
        @SerializedName("itemSoldPaymentVerified")
        val itemSoldPaymentVerified: String = ""
) {

    private val getTxSuccessInt
        get() = txSuccess.toIntOrNull() ?: 0

    private val getTxRejectInt
        get() = txReject.toIntOrNull() ?: 0

    val getSuccessRate: Float
        get() = if (getTxSuccessInt == 0 && getTxRejectInt == 0) 0f
        else 100f * getTxSuccessInt.toFloat() / (getTxSuccessInt + getTxRejectInt).toFloat()

    val getSuccessRateRound: String
        get() = DecimalFormat("##.#").format(getSuccessRate)

}