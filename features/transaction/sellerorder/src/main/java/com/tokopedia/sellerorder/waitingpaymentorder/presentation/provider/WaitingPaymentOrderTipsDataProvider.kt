package com.tokopedia.sellerorder.waitingpaymentorder.presentation.provider

import android.content.Context
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderTipsUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderTipsDataProvider {

    companion object {
        private const val URL_IMAGE_WAITING_PAYMENT_ORDER_TIPS_BEST_SELLING = "https://ecs7.tokopedia.net/android/others/ic_waiting_payment_order_tips_best_selling.png"
        private const val URL_IMAGE_WAITING_PAYMENT_ORDER_TIPS_EMPTY_STOCK = "https://ecs7.tokopedia.net/android/others/ic_waiting_payment_order_tips_empty_stock.png"
    }

    fun provideData(context: Context): List<WaitingPaymentOrderTipsUiModel> {
        return listOf(
                WaitingPaymentOrderTipsUiModel(
                        iconUrl = URL_IMAGE_WAITING_PAYMENT_ORDER_TIPS_BEST_SELLING,
                        description = context.getString(R.string.bottomsheet_waiting_payment_tips_best_selling_description)
                ),
                WaitingPaymentOrderTipsUiModel(
                        iconUrl = URL_IMAGE_WAITING_PAYMENT_ORDER_TIPS_EMPTY_STOCK,
                        description = context.getString(R.string.bottomsheet_waiting_payment_tips_empty_stock_description)
                )
        )
    }
}