package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageProductCardsModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemProductCardsAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageViewModel.Companion.SECTION_PRODUCT_CARD_ROW_1X1
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_product_cards.view.*

/**
 * @author by resakemal on 22/06/20.
 */

class RechargeHomepageProductCardsViewHolder(itemView: View, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageProductCardsModel>(itemView) {

    override fun bind(element: RechargeHomepageProductCardsModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                view_recharge_home_product_cards_container.show()
                view_recharge_home_product_cards_shimmering.hide()

                rv_recharge_home_product_cards.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                rv_recharge_home_product_cards.adapter = RechargeItemProductCardsAdapter(section.items, listener,
                        section.template == SECTION_PRODUCT_CARD_ROW_1X1)

                tv_recharge_home_product_cards_title.text = section.title
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                view_recharge_home_product_cards_container.hide()
                view_recharge_home_product_cards_shimmering.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_product_cards
    }
}