package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageLegoBannerModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_travel_homepage_lego_banner.view.*

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageLegoBannerViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val onItemClickListener: OnItemClickListener) : AbstractViewHolder<TravelHomepageLegoBannerModel>(itemView) {
    override fun bind(element: TravelHomepageLegoBannerModel) {
        if (element.isLoaded) {
            if (element.bannerItem.isNotEmpty()) {
                with(itemView) {
                    legoBannerWidget.titleText = element.title
                    legoBannerWidget.subtitleText = element.subtitle
                    legoBannerWidget.buildView(element.bannerItem)
                }
            } else {
                // hide shimmering and hide layout
            }
        } else {
            // show shimmering hide layout
            //
            onItemBindListener.onItemBindViewHolder(element.layoutData, element.isLoadFromCloud)
        }

    }

    companion object {
        val LAYOUT = R.layout.layout_travel_homepage_lego_banner
    }

}