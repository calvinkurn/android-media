package com.tokopedia.travel.homepage.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.homepage.data.*
import com.tokopedia.travel.homepage.presentation.adapter.viewholder.TravelHomepageBannerViewHolder

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageAdapterTypeFactory : BaseAdapterTypeFactory(), TravelHomepageTypeFactory {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<Visitable<*>> {
        return super.createViewHolder(parent, type)
    }

    override fun type(viewModel: TravelHomepageBannerModel): Int = TravelHomepageBannerViewHolder.LAYOUT

    override fun type(viewModel: TravelHomepageCategoryListModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: TravelHomepageDestinationModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: TravelHomepageOrderListModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: TravelHomepageRecentSearchModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: TravelHomepageRecommendationModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}