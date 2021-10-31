package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeTopStatusBinding
import com.tokopedia.buyerorder.recharge.presentation.customview.RechargeOrderDetailSimpleView
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailTopSectionModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailTopSectionViewHolder(
        val binding: ItemOrderDetailRechargeTopStatusBinding,
        val listener: RechargeOrderDetailTopSectionActionListener?
) : AbstractViewHolder<RechargeOrderDetailTopSectionModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailTopSectionModel) {
        with(binding) {
            tgRechargeOrderDetailStatus.text = element.textStatus

            if (element.tickerData.text.isNotEmpty() or element.tickerData.title.isNotEmpty()) {
                tickerRechargeOrderDetail.setTextDescription(element.tickerData.text)
                tickerRechargeOrderDetail.show()
            } else {
                tickerRechargeOrderDetail.hide()
            }

            tgRechargeInvoiceNumber.text = element.invoiceRefNum
            icRechargeInvoiceCopy.setOnClickListener {
                listener?.onCopyInvoiceNumberClicked(element.invoiceRefNum)
            }
            tgRechargeSeeInvoice.setOnClickListener {
                RouteManager.route(root.context, element.invoiceUrl)
            }

            for (item in element.titleData) {
                val simpleView = RechargeOrderDetailSimpleView(root.context)
                simpleView.setData(item)
                containerRechargeOrderDetailTopSectionTitles.addView(simpleView)
            }
        }
    }

    interface RechargeOrderDetailTopSectionActionListener {
        fun onCopyInvoiceNumberClicked(invoiceRefNum: String)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_top_status
    }

}