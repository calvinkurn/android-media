package com.tokopedia.travel.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.data.*
import com.tokopedia.travel.homepage.presentation.adapter.viewholder.*
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageAdapterTypeFactory(val onBindListener: OnItemBindListener, val onItemClickListener: OnItemClickListener) : BaseAdapterTypeFactory(), TravelHomepageTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TravelHomepageBannerViewHolder.LAYOUT -> return TravelHomepageBannerViewHolder(parent, onBindListener)
            TravelHomepageCategoryViewHolder.LAYOUT -> return TravelHomepageCategoryViewHolder(parent, onBindListener)
            TravelHomepageSectionViewHolder.LAYOUT -> return TravelHomepageSectionViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageDestinationViewHolder.LAYOUT -> return TravelHomepageDestinationViewHolder(parent, onBindListener, onItemClickListener)
        }
        return super.createViewHolder(parent, type)
    }

    override fun type(viewModel: TravelHomepageBannerModel): Int = TravelHomepageBannerViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageCategoryListModel): Int = TravelHomepageCategoryViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageDestinationModel): Int = TravelHomepageDestinationViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageSectionViewModel): Int = TravelHomepageSectionViewHolder.LAYOUT
}