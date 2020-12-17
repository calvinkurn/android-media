package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderTipsUiModel
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.bottomsheet_waiting_payment_order_tips_item.view.*

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderTipsViewHolder(itemView: View?) : AbstractViewHolder<WaitingPaymentOrderTipsUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.bottomsheet_waiting_payment_order_tips_item
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderTipsUiModel?) {
        element?.let { element ->
            with(itemView) {
                ivWaitingOrderTips.setImage(element.iconUrl, 0f)
                tvWaitingOrderTipsDescription.text = element.description.parseAsHtml()
            }
        }
    }
}