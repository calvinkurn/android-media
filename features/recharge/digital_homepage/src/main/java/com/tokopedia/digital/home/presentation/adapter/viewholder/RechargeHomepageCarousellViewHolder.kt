package com.tokopedia.digital.home.presentation.adapter.viewholder


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageCarousellModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemCarousellAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_carousell.view.*

/**
 * @author by firman on 09/03/21.
 */

class RechargeHomepageCarousellViewHolder(itemView: View, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageCarousellModel>(itemView) {

    override fun bind(element: RechargeHomepageCarousellModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                view_container_recharge_home_carousell.show()
                view_recharge_home_carousell_shimmering.hide()

                tv_recharge_home_carousell_title.text = section.title
                val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                rv_recharge_home_carousell.apply {
                    this.layoutManager = layoutManager
                    adapter = RechargeItemCarousellAdapter(section.items, listener)
                }
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                view_container_recharge_home_carousell.hide()
                view_recharge_home_carousell_shimmering.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_carousell
    }
}