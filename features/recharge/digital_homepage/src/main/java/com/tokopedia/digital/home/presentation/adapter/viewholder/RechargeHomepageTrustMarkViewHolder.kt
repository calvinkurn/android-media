package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageDualIconsModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemTrustMarkAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_trustmark.view.*

class RechargeHomepageTrustMarkViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageDualIconsModel>(itemView) {

    override fun bind(element: RechargeHomepageDualIconsModel) {
        with (itemView) {
            if (element.items.isEmpty()) {
                digital_homepage_trust_mark_container.hide()
            } else {
                digital_homepage_trust_mark_container.show()

                val layoutManager = GridLayoutManager(context, TRUST_MARK_SPAN_COUNT)
                rv_digital_homepage_trust_mark.layoutManager = layoutManager

                while (rv_digital_homepage_trust_mark.itemDecorationCount > 0) {
                    rv_digital_homepage_trust_mark.removeItemDecorationAt(0)
                }
                rv_digital_homepage_trust_mark.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

                // Only use first 2 items or less
                val trustMarkItems: List<RechargeHomepageSections.Item> = when (element.items.size) {
                    in 1..2 -> element.items
                    else -> element.items.subList(0, 2)
                }
                rv_digital_homepage_trust_mark.adapter =
                        RechargeItemTrustMarkAdapter(trustMarkItems)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_trustmark
        const val TRUST_MARK_SPAN_COUNT = 2
    }
}