package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageLegoBannerModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import com.tokopedia.travelhomepage.homepage.widget.TravelHomepageLegoBannerWidget
import kotlinx.android.synthetic.main.layout_travel_homepage_lego_banner.view.*

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageLegoBannerViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val travelHomepageActionListener: TravelHomepageActionListener) : AbstractViewHolder<TravelHomepageLegoBannerModel>(itemView) {
    override fun bind(element: TravelHomepageLegoBannerModel) {
        if (element.isLoaded) {
            if (element.isSuccess && element.bannerItem.isNotEmpty()) {
                with(itemView) {
                    legoBannerWidget.setShimmeringVisibility(false)
                    legoBannerWidget.setLayoutVisibility(true)

                    legoBannerWidget.titleText = element.title
                    legoBannerWidget.subtitleText = element.subtitle
                    legoBannerWidget.listener = object: TravelHomepageLegoBannerWidget.ActionListener{
                        override fun onItemClickListener(item: LegoBannerItemModel) {
                            travelHomepageActionListener.onItemClick(item.appUrl, item.webUrl)
                        }
                    }
                    legoBannerWidget.buildView(element.bannerItem)
                }
            } else {
                // hide shimmering and hide layout
                itemView.legoBannerWidget.buildView(listOf())
                itemView.legoBannerWidget.setShimmeringVisibility(false)
                itemView.legoBannerWidget.setLayoutVisibility(false)
            }
        } else {
            // show shimmering hide layout
            itemView.legoBannerWidget.setShimmeringVisibility(true)
            itemView.legoBannerWidget.setLayoutVisibility(false)

            onItemBindListener.onLegoBannerItemBind(element.layoutData, adapterPosition, element.isLoadFromCloud)
        }

    }

    companion object {
        val LAYOUT = R.layout.layout_travel_homepage_lego_banner
    }

}