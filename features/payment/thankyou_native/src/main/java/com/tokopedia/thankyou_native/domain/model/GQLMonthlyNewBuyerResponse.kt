package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GQLMonthlyNewBuyerResponse(
        @SerializedName("is_buyer_first_monthly_trx")
        @Expose
        val monthlyNewBuyer: MonthlyNewBuyer
)

data class MonthlyNewBuyer(
        @SerializedName("order_id")
        @Expose
        val orderId: Int,
        @SerializedName("payment_id")
        @Expose
        val paymentId: Int,
        @SerializedName("is_monthly_first_transaction")
        @Expose
        val isMonthlyFirstTransaction: Int
)
