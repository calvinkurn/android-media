package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeFavoritesBinding
import com.tokopedia.digital.home.model.RechargeHomepageFavoriteModel
import com.tokopedia.digital.home.presentation.adapter.RechargeItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by resakemal on 05/06/20.
 */


class RechargeHomepageFavoriteViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageFavoriteModel>(itemView) {

    override fun bind(element: RechargeHomepageFavoriteModel) {
        val bind = ViewRechargeHomeFavoritesBinding.bind(itemView)
        val section = element.section
        with(bind) {
            if (section.items.isNotEmpty()) {
                viewRechargeHomeFavoritesShimmering.root.hide()

                val layoutManager = GridLayoutManager(root.context, FAVORITES_SPAN_COUNT)
                rvRechargeHomeFavorites.layoutManager = layoutManager
                rechargeHomeFavoritesContainer.show()
                tvRechargeHomeFavoritesTitle.text = section.title
                rvRechargeHomeFavorites.adapter = RechargeItemFavoriteAdapter(section.items, listener)
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            } else {
                viewRechargeHomeFavoritesShimmering.root.show()
                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_favorites
        const val FAVORITES_SPAN_COUNT = 5
    }
}