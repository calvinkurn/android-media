package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeAboutOrdersBinding
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailAboutOrderModel

/**
 * @author by furqan on 02/11/2021
 */
class RechargeOrderDetailAboutOrderViewHolder(
        private val binding: ItemOrderDetailRechargeAboutOrdersBinding,
        private val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailAboutOrderModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailAboutOrderModel) {
        with(binding) {
            containerRechargeOrderDetailHelp.setOnClickListener {
                listener?.onClickHelp(element.helpUrl)
                RouteManager.route(root.context, element.helpUrl)
            }
        }
    }

    interface ActionListener {
        fun onClickHelp(helpUrl: String)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_about_orders
    }
}