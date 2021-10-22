package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemWaitingPaymentOrdersErrorBinding
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.ErrorType
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderErrorNetworkUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrdersErrorViewHolder(
        itemView: View
) : AbstractViewHolder<WaitingPaymentOrderErrorNetworkUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders_error
    }

    private val binding by viewBinding<ItemWaitingPaymentOrdersErrorBinding>()

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderErrorNetworkUiModel?) {
        binding?.run {
            element?.let { element ->
                when (element.errorType) {
                    ErrorType.NO_CONNECTION -> waitingPaymentOrderGlobalError.setType(NO_CONNECTION)
                    ErrorType.SERVER_ERROR -> waitingPaymentOrderGlobalError.setType(SERVER_ERROR)
                    else -> waitingPaymentOrderGlobalError.setType(SERVER_ERROR)
                }
                waitingPaymentOrderGlobalError.setActionClickListener {
                    element.onRetryListener.onRetryClicked()
                }
            }
        }
    }
}