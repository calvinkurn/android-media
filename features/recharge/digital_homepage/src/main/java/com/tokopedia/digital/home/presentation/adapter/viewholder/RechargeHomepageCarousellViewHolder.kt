package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeCarousellBinding
import com.tokopedia.digital.home.model.RechargeHomepageCarousellModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemCarousellAdapter
import com.tokopedia.digital.home.presentation.adapter.decoration.RechargeItemProductCardsDecorator
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by firman on 09/03/21.
 */

class RechargeHomepageCarousellViewHolder(itemView: View, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageCarousellModel>(itemView) {

    override fun bind(element: RechargeHomepageCarousellModel) {
        val bind = ViewRechargeHomeCarousellBinding.bind(itemView)
        val section = element.section
        with(bind) {
            if (section.items.isNotEmpty()) {
                viewContainerRechargeHomeCarousell.show()
                viewRechargeHomeCarousellShimmering.root.hide()

                tvRechargeHomeCarousellTitle.text = section.title
                val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                val displayMetrics = itemView.context.resources.displayMetrics
                rvRechargeHomeCarousell.apply {
                    while (itemDecorationCount > 0) {
                        removeItemDecorationAt(0)
                    }
                    this.layoutManager = layoutManager
                    adapter = RechargeItemCarousellAdapter(section.items, listener)
                    addItemDecoration(
                        RechargeItemProductCardsDecorator(
                            PRODUCT_CARDS_SPACE_DP.dpToPx(displayMetrics)
                    )
                    )
                }
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                viewContainerRechargeHomeCarousell.hide()
                viewRechargeHomeCarousellShimmering.root.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_carousell
        const val PRODUCT_CARDS_SPACE_DP = 8
    }
}