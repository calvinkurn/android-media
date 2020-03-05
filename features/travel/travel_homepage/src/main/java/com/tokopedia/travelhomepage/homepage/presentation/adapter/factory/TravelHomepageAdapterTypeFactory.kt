package com.tokopedia.travelhomepage.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder.*
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener

/**
 * @author by furqan on 06/08/2019
 */
open class TravelHomepageAdapterTypeFactory (private val onBindListener: OnItemBindListener, private val onItemClickListener: OnItemClickListener) : BaseAdapterTypeFactory(), TravelHomepageTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TravelHomepageBannerViewHolder.LAYOUT -> return TravelHomepageBannerViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageCategoryViewHolder.LAYOUT -> return TravelHomepageCategoryViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageSectionViewHolder.LAYOUT -> return TravelHomepageSectionViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageDestinationViewHolder.LAYOUT -> return TravelHomepageDestinationViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageProductCardViewHolder.LAYOUT -> return TravelHomepageProductCardViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageLegoBannerViewHolder.LAYOUT -> return TravelHomepageLegoBannerViewHolder(parent, onBindListener, onItemClickListener)
        }
        return super.createViewHolder(parent, type)
    }

    override fun type(viewModel: TravelHomepageBannerModel): Int = TravelHomepageBannerViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageCategoryListModel): Int = TravelHomepageCategoryViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageDestinationModel): Int = TravelHomepageDestinationViewHolder.LAYOUT

    override fun type(model: TravelHomepageSectionModel): Int = TravelHomepageSectionViewHolder.LAYOUT

    override fun type(model: TravelHomepageProductCardModel): Int = TravelHomepageProductCardViewHolder.LAYOUT

    override fun type(model: TravelHomepageLegoBannerModel): Int = TravelHomepageLegoBannerViewHolder.LAYOUT
}