package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_IMPRESSION
import com.tokopedia.digital.home.presentation.adapter.adapter.RechargeItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_favorites.view.*

class RechargeHomepageFavoriteViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageSections.Section>(itemView) {

    override fun bind(element: RechargeHomepageSections.Section) {
        val layoutManager = GridLayoutManager(itemView.context, FAVORITES_SPAN_COUNT)
        itemView.rv_digital_homepage_favorites.layoutManager = layoutManager
        itemView.digital_homepage_favorites_container.show()
        itemView.digital_homepage_favorites_title.text = element.title
        itemView.rv_digital_homepage_favorites.adapter = RechargeItemFavoriteAdapter(element.items, onItemBindListener)
        onItemBindListener.onRechargeSectionItemImpression(element.items, BEHAVIORAL_CATEGORY_IMPRESSION)
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_favorites
        const val FAVORITES_SPAN_COUNT = 5
    }
}