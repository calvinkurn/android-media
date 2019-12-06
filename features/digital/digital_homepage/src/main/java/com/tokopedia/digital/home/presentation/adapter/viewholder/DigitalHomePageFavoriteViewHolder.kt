package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageFavoritesModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_IMPRESSION
import com.tokopedia.digital.home.presentation.adapter.adapter.DigitalItemFavoriteAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_favorites.view.*

class DigitalHomePageFavoriteViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageFavoritesModel>(itemView) {

    override fun bind(element: DigitalHomePageFavoritesModel) {
        val layoutManager = GridLayoutManager(itemView.context, FAVORITES_SPAN_COUNT)
        itemView.rv_digital_homepage_favorites.layoutManager = layoutManager
        if (element.isLoaded) {
            if (element.isSuccess) {
                element.data?.section?.run {
                    itemView.digital_homepage_favorites_shimmering.hide()
                    itemView.digital_homepage_favorites_container.show()
                    itemView.digital_homepage_favorites_title.text = title
                    itemView.rv_digital_homepage_favorites.adapter = DigitalItemFavoriteAdapter(items, onItemBindListener)
                    onItemBindListener.onSectionItemImpression(items, BEHAVIORAL_CATEGORY_IMPRESSION, true)
                }
            } else {
                itemView.digital_homepage_favorites_shimmering.hide()
                itemView.digital_homepage_favorites_container.hide()
            }
        } else {
            itemView.digital_homepage_favorites_shimmering.show()
            itemView.digital_homepage_favorites_container.hide()
            onItemBindListener.onFavoritesItemDigitalBind(element.isLoadFromCloud)
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_favorites
        const val FAVORITES_SPAN_COUNT = 5
    }
}