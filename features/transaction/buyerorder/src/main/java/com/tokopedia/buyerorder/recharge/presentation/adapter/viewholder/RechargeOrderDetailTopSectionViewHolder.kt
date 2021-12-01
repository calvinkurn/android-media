package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeTopStatusBinding
import com.tokopedia.buyerorder.recharge.presentation.customview.RechargeOrderDetailSimpleView
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailTopSectionModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ticker.TickerCallback

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailTopSectionViewHolder(
        val binding: ItemOrderDetailRechargeTopStatusBinding,
        val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailTopSectionModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailTopSectionModel) {
        with(binding) {
            tgRechargeOrderDetailStatus.text = element.textStatus

            if (element.tickerData.text.isNotEmpty()) {
                tickerRechargeOrderDetail.setHtmlDescription(element.tickerData.text)
                tickerRechargeOrderDetail.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        if (linkUrl.isNotEmpty()) {
                            RouteManager.route(root.context, linkUrl.toString())
                        } else {
                            RouteManager.route(root.context, element.tickerData.urlDetail)
                        }
                    }

                    override fun onDismiss() {
                        // no op
                    }
                })
                tickerRechargeOrderDetail.show()
            } else {
                tickerRechargeOrderDetail.hide()
            }

            tgRechargeInvoiceNumber.text = element.invoiceRefNum
            icRechargeInvoiceCopy.setOnClickListener {
                listener?.onCopyInvoiceNumberClicked(element.invoiceRefNum)
            }
            tgRechargeSeeInvoice.setOnClickListener {
                listener?.onSeeInvoiceClicked(element.invoiceRefNum, element.invoiceUrl)
                RouteManager.route(root.context, element.invoiceUrl)
            }

            if (containerRechargeOrderDetailTopSectionTitles.childCount < element.titleData.size) {
                containerRechargeOrderDetailTopSectionTitles.removeAllViews()
                for (item in element.titleData) {
                    val simpleView = RechargeOrderDetailSimpleView(root.context)
                    simpleView.setData(item)
                    containerRechargeOrderDetailTopSectionTitles.addView(simpleView)
                }
            }
        }
    }

    interface ActionListener {
        fun onCopyInvoiceNumberClicked(invoiceRefNum: String)
        fun onSeeInvoiceClicked(invoiceRefNum: String, invoiceUrl: String)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_top_status
    }

}