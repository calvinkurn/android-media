package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageTrustMarkModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemDualIconsAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_dual_icons.view.*

/**
 * @author by resakemal on 09/06/20.
 */

class RechargeHomepageDualIconsViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageTrustMarkModel>(itemView) {

    override fun bind(element: RechargeHomepageTrustMarkModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                view_recharge_home_dual_icons_container.show()
                view_recharge_home_dual_icons_shimmering.hide()

                while (rv_recharge_home_dual_icons.itemDecorationCount > 0) {
                    rv_recharge_home_dual_icons.removeItemDecorationAt(0)
                }
                rv_recharge_home_dual_icons.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

                // Only use first 3 items or less
                val trustMarkItems: List<RechargeHomepageSections.Item> = when (section.items.size) {
                    in 1..TRUST_MARK_MAX_SPAN_COUNT -> section.items
                    else -> section.items.subList(0, 3)
                }

                val layoutManager = GridLayoutManager(context, trustMarkItems.size)
                rv_recharge_home_dual_icons.layoutManager = layoutManager

                rv_recharge_home_dual_icons.adapter =
                        RechargeItemDualIconsAdapter(trustMarkItems, listener)

                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                view_recharge_home_dual_icons_container.hide()
                view_recharge_home_dual_icons_shimmering.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_dual_icons
        const val TRUST_MARK_MAX_SPAN_COUNT = 3
    }
}