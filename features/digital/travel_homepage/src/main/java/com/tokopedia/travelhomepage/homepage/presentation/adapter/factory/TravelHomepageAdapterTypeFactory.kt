package com.tokopedia.travelhomepage.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionViewModel
import com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder.TravelHomepageBannerViewHolder
import com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder.TravelHomepageCategoryViewHolder
import com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder.TravelHomepageDestinationViewHolder
import com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder.TravelHomepageSectionViewHolder
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageAdapterTypeFactory(private val onBindListener: OnItemBindListener, private val onItemClickListener: OnItemClickListener) : BaseAdapterTypeFactory(), TravelHomepageTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TravelHomepageBannerViewHolder.LAYOUT -> return TravelHomepageBannerViewHolder(parent, onBindListener, onItemClickListener)
            TravelHomepageCategoryViewHolder.LAYOUT -> return TravelHomepageCategoryViewHolder(parent, onBindListener, onItemClickListener)
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