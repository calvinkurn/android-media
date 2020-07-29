package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageTrustMarkModel
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemTrustMarkAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_trustmark.view.*

/**
 * @author by resakemal on 09/06/20.
 */

class RechargeHomepageTrustMarkViewHolder(itemView: View?, val listener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageTrustMarkModel>(itemView) {

    override fun bind(element: RechargeHomepageTrustMarkModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                view_recharge_home_trust_mark_container.show()

                while (rv_recharge_home_trust_mark.itemDecorationCount > 0) {
                    rv_recharge_home_trust_mark.removeItemDecorationAt(0)
                }
                rv_recharge_home_trust_mark.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

                // Only use first 3 items or less
                val trustMarkItems: List<RechargeHomepageSections.Item> = when (section.items.size) {
                    in 1..3 -> section.items
                    else -> section.items.subList(0, 3)
                }

                val layoutManager = GridLayoutManager(context, trustMarkItems.size)
                rv_recharge_home_trust_mark.layoutManager = layoutManager

                rv_recharge_home_trust_mark.adapter =
                        RechargeItemTrustMarkAdapter(trustMarkItems)

            } else {
                listener.onRechargeSectionEmpty(adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_trustmark
        const val TRUST_MARK_SPAN_COUNT = 2
    }
}