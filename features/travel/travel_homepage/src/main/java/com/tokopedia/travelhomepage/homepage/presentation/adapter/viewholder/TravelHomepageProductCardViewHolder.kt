package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageProductCardModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_travel_homepage_product_card.view.*

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageProductCardViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val onItemClickListener: OnItemClickListener) : AbstractViewHolder<TravelHomepageProductCardModel>(itemView) {
    override fun bind(element: TravelHomepageProductCardModel) {
        if (element.isLoaded) {
            if (element.productItem.isNotEmpty()) {
                with(itemView) {
                    productCardWidget.titleText = element.title
                    productCardWidget.subtitleText = element.subtitle
                    productCardWidget.hasSeeAllButton = element.clickSeeAllUrl.isEmpty()
                    productCardWidget.buildView(element.productItem)
                }
            } else {
                // hide shimmering and hide layout
                itemView.productCardWidget.setShimmeringVisibility(false)
                itemView.productCardWidget.setLayoutVisibility(false)
            }
        } else {
            // show shimmering hide layout
            itemView.productCardWidget.setShimmeringVisibility(true)
            itemView.productCardWidget.setLayoutVisibility(false)
            onItemBindListener.onItemBindViewHolder(element.layoutData, adapterPosition, element.isLoadFromCloud)
        }

    }

    companion object {
        val LAYOUT = R.layout.layout_travel_homepage_product_card
    }

}