package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailDividerModel

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailDividerViewHolder(itemView: View?)
    : AbstractViewHolder<RechargeOrderDetailDividerModel>(itemView) {

    override fun bind(element: RechargeOrderDetailDividerModel?) {
        // no op
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_divider
    }
}