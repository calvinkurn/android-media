package com.tokopedia.travel.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.data.*
import com.tokopedia.travel.homepage.data.TravelHomepageItemModel
import com.tokopedia.travel.homepage.presentation.adapter.viewholder.*
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageAdapterTypeFactory(val onBindListener: OnItemBindListener) : BaseAdapterTypeFactory(), TravelHomepageTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TravelHomepageBannerViewHolder.LAYOUT -> return TravelHomepageBannerViewHolder(parent, onBindListener)
            TravelHomepageCategoryViewHolder.LAYOUT -> return TravelHomepageCategoryViewHolder(parent, onBindListener)
            TravelHomepageOrderListViewHolder.LAYOUT -> return TravelHomepageOrderListViewHolder(parent, onBindListener)
            TravelHomepageRecentSearchViewHolder.LAYOUT -> return TravelHomepageRecentSearchViewHolder(parent, onBindListener)
            TravelHomepageRecommendationViewHolder.LAYOUT -> return TravelHomepageRecommendationViewHolder(parent, onBindListener)
        }
        return super.createViewHolder(parent, type)
    }

    override fun type(viewModel: TravelHomepageBannerModel): Int = TravelHomepageBannerViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageCategoryListModel): Int = TravelHomepageCategoryViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageDestinationModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: TravelHomepageOrderListModel): Int = TravelHomepageOrderListViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageRecentSearchModel): Int = TravelHomepageRecentSearchViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageRecommendationModel): Int = TravelHomepageRecommendationViewHolder.LAYOUT
}