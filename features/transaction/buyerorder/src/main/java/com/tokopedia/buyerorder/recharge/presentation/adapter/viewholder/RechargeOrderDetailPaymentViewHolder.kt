package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargePaymentDetailBinding
import com.tokopedia.buyerorder.recharge.presentation.customview.RechargeOrderDetailSimpleView
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailPaymentModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailPaymentViewHolder(
        private val binding: ItemOrderDetailRechargePaymentDetailBinding
) : AbstractViewHolder<RechargeOrderDetailPaymentModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailPaymentModel) {
        with(binding) {
            simpleRechargeOrderDetailPaymentMethod.setData(element.paymentMethod)

            if (containerRechargeOrderDetailPaymentDetail.childCount < element.paymentDetails.size) {
                containerRechargeOrderDetailPaymentDetail.removeAllViews()
                for (item in element.paymentDetails) {
                    val simpleView = RechargeOrderDetailSimpleView(root.context)
                    simpleView.setData(item)
                    containerRechargeOrderDetailPaymentDetail.addView(simpleView)
                }
            }

            tgRechargeOrderDetailTotalPriceLabel.text = element.totalPriceLabel
            tgRechargeOrderDetailTotalPrice.text = element.totalPrice

            tickerRechargeOrderDetailPayment.hide()
            element.additionalTicker?.let {
                tickerRechargeOrderDetailPayment.tickerTitle = it.title
                tickerRechargeOrderDetailPayment.setHtmlDescription(it.text)
                tickerRechargeOrderDetailPayment.show()
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_payment_detail
    }

}