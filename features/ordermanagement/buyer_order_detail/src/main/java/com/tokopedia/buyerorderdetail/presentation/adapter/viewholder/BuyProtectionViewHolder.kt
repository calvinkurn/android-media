package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.BuyProtectionUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_buy_protection.view.*
import java.util.*

class BuyProtectionViewHolder(
        itemView: View?,
        private val listener: BuyProtectionListener) : AbstractViewHolder<BuyProtectionUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_buy_protection
    }

    override fun bind(element: BuyProtectionUiModel?) {
        element?.let {
            setupTitle(it.title)
            setupDescription(it.description)
            setupDeadlineTimer(it.deadline)
        }
    }

    private fun setupTitle(title: String) {
        itemView.tvBuyerOrderDetailBuyProtectionLabel?.text = title
    }

    private fun setupDescription(description: String) {
        itemView.tvBuyerOrderDetailBuyProtectionDescription?.text = description
    }

    private fun setupDeadlineTimer(deadline: Long) {
        itemView.tvBuyerOrderDetailBuyProtectionTimer?.apply {
            targetDate = Calendar.getInstance().apply {
                timeInMillis = deadline
            }
            onFinish = ::onReachBuyProtectionDeadline
            resume()
        }
    }

    private fun onReachBuyProtectionDeadline() {
        listener.onReachBuyProtectionDeadline()
    }

    interface BuyProtectionListener {
        fun onClickBuyProtection()
        fun onReachBuyProtectionDeadline()
    }
}