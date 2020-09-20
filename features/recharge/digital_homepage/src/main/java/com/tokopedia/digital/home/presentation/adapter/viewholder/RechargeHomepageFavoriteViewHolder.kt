package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageFavoriteModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_recharge_home_favorites.view.*

/**
 * @author by resakemal on 05/06/20.
 */


class RechargeHomepageFavoriteViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageFavoriteModel>(itemView) {

    override fun bind(element: RechargeHomepageFavoriteModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                view_recharge_home_favorites_shimmering.hide()

                val layoutManager = GridLayoutManager(context, FAVORITES_SPAN_COUNT)
                rv_recharge_home_favorites.layoutManager = layoutManager
                recharge_home_favorites_container.show()
                tv_recharge_home_favorites_title.text = section.title
                rv_recharge_home_favorites.adapter = RechargeItemFavoriteAdapter(section.items, listener)
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                view_recharge_home_favorites_shimmering.show()
                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_favorites
        const val FAVORITES_SPAN_COUNT = 5
    }
}