package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageProductCardModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import com.tokopedia.travelhomepage.homepage.widget.TravelHomepageProductGridCardWidget
import kotlinx.android.synthetic.main.layout_travel_homepage_product_card.view.*

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageProductCardViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val travelHomepageActionListener: TravelHomepageActionListener) : AbstractViewHolder<TravelHomepageProductCardModel>(itemView) {

    private var currentPosition = 0

    override fun bind(element: TravelHomepageProductCardModel) {
        if (element.isLoaded) {
            if (element.isSuccess && element.productItem.isNotEmpty()) {
                with(itemView) {
                    productCardWidget.setShimmeringVisibility(false)
                    productCardWidget.setLayoutVisibility(true)

                    productCardWidget.titleText = element.title
                    productCardWidget.subtitleText = element.subtitle
                    productCardWidget.hasSeeAllButton = element.clickSeeAllUrl.isNotEmpty()
                    productCardWidget.buttonText = element.layoutData.metaText
                    productCardWidget.listener = object: TravelHomepageProductGridCardWidget.ActionListener {
                        override fun onItemClickListener(item: ProductGridCardItemModel, position: Int) {
                            travelHomepageActionListener.onClickProductCard(item, position, element.layoutData.position, element.title)
                            travelHomepageActionListener.onItemClick(item.appUrl)
                        }

                        override fun onClickSeeAllListener() {
                            travelHomepageActionListener.onClickSeeAllProductCards(element.layoutData.position, element.title)
                            travelHomepageActionListener.onItemClick(element.clickSeeAllUrl)
                        }
                    }
                    if (currentPosition != element.layoutData.position) {
                        currentPosition = element.layoutData.position
                        travelHomepageActionListener.onViewProductCards(element.productItem, element.layoutData.position, element.title)
                        productCardWidget.buildView(element.productItem)
                    }
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
            currentPosition = -1
            onItemBindListener.onProductCardItemBind(element.layoutData, element.isLoadFromCloud)
        }

    }

    companion object {
        val LAYOUT = R.layout.layout_travel_homepage_product_card
    }

}