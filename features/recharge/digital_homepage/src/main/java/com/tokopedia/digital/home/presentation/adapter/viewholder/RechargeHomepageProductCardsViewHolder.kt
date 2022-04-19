package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardsBinding
import com.tokopedia.digital.home.model.RechargeHomepageProductCardsModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemProductCardsAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageViewModel.Companion.SECTION_PRODUCT_CARD_ROW_1X1
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by resakemal on 22/06/20.
 */

class RechargeHomepageProductCardsViewHolder(itemView: View, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageProductCardsModel>(itemView) {

    override fun bind(element: RechargeHomepageProductCardsModel) {
        val bind = ViewRechargeHomeProductCardsBinding.bind(itemView)
        val section = element.section
        with(bind) {
            if (section.items.isNotEmpty()) {
                viewRechargeHomeProductCardsContainer.show()
                viewRechargeHomeProductCardsShimmering.root.hide()

                rvRechargeHomeProductCards.layoutManager = LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)

                rvRechargeHomeProductCards.adapter = RechargeItemProductCardsAdapter(section.items, listener,
                        section.template == SECTION_PRODUCT_CARD_ROW_1X1)

                tvRechargeHomeProductCardsTitle.text = section.title
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                viewRechargeHomeProductCardsContainer.hide()
                viewRechargeHomeProductCardsShimmering.root.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_product_cards
    }
}