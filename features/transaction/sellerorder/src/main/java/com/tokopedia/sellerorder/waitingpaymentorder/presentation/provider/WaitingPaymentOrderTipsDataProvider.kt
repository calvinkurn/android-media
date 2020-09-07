package com.tokopedia.sellerorder.waitingpaymentorder.presentation.provider

import android.content.Context
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderTipsUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderTipsDataProvider {
    fun provideData(context: Context): List<WaitingPaymentOrderTipsUiModel> {
        return listOf(
                WaitingPaymentOrderTipsUiModel(
                        icon = R.drawable.ic_waiting_payment_order_tips_best_selling,
                        description = context.getString(R.string.bottomsheet_waiting_payment_tips_best_selling_description)
                ),
                WaitingPaymentOrderTipsUiModel(
                        icon = R.drawable.ic_waiting_payment_order_tips_empty_stock,
                        description = context.getString(R.string.bottomsheet_waiting_payment_tips_empty_stock_description)
                )
        )
    }
}