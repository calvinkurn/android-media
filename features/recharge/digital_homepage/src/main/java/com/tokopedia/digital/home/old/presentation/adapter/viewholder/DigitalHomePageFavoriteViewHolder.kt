package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeFavoritesBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageFavoritesModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_IMPRESSION
import com.tokopedia.digital.home.old.presentation.adapter.adapter.DigitalItemFavoriteAdapter
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalHomePageFavoriteViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageFavoritesModel>(itemView) {

    override fun bind(element: DigitalHomePageFavoritesModel) {
        val bind = LayoutDigitalHomeFavoritesBinding.bind(itemView)
        val layoutManager = GridLayoutManager(itemView.context, FAVORITES_SPAN_COUNT)
        bind.rvDigitalHomepageFavorites.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    bind.viewDigitalHomepageFavoritesShimmering.root.hide()
                    bind.viewDigitalHomepageFavoritesContainer.show()
                    bind.tvDigitalHomepageFavoritesTitle.text = title
                    bind.rvDigitalHomepageFavorites.adapter = DigitalItemFavoriteAdapter(items, onItemBindListener)
                    onItemBindListener.onSectionItemImpression(items, BEHAVIORAL_CATEGORY_IMPRESSION)
                }
            } else {
                bind.viewDigitalHomepageFavoritesShimmering.root.hide()
                bind.viewDigitalHomepageFavoritesContainer.hide()
            }
        } else {
            bind.viewDigitalHomepageFavoritesShimmering.root.show()
            bind.viewDigitalHomepageFavoritesContainer.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_favorites
        const val FAVORITES_SPAN_COUNT = 5
    }
}