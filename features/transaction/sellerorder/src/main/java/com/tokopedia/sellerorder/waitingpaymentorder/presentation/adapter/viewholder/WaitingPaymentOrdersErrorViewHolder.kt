package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderErrorNetworkUiModel
import kotlinx.android.synthetic.main.item_waiting_payment_orders_error.view.*

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrdersErrorViewHolder(
        itemView: View?
) : AbstractViewHolder<WaitingPaymentOrderErrorNetworkUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders_error
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderErrorNetworkUiModel?) {
        element?.let { element ->
            when (element.errorType) {
                0 -> itemView.waitingPaymentOrderGlobalError.setType(NO_CONNECTION)
                1 -> itemView.waitingPaymentOrderGlobalError.setType(SERVER_ERROR)
                else -> itemView.waitingPaymentOrderGlobalError.setType(SERVER_ERROR)
            }
            itemView.waitingPaymentOrderGlobalError.setActionClickListener {
                element.onRetryListener.onRetryClicked()
            }
        }
    }
}