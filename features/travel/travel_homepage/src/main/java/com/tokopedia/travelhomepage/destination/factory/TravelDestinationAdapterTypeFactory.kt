package com.tokopedia.travelhomepage.destination.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.destination.listener.ActionListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder.TravelDestinationArticleViewHolder
import com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder.TravelDestinationCityRecommendationViewHolder
import com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder.TravelDestinationSectionViewHolder
import com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder.TravelDestinationSummaryViewHolder
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_RECOMMENDATION_ORDER
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelDestinationTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
open class TravelDestinationAdapterTypeFactory(private val onBindListener: OnViewHolderBindListener,
                                          private val itemActionListener: ActionListener)
    : BaseAdapterTypeFactory(), TravelDestinationTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TravelDestinationSummaryViewHolder.LAYOUT -> return TravelDestinationSummaryViewHolder(parent, onBindListener)
            TravelDestinationCityRecommendationViewHolder.LAYOUT -> return TravelDestinationCityRecommendationViewHolder(parent, onBindListener, itemActionListener)
            TravelDestinationSectionViewHolder.LAYOUT -> return TravelDestinationSectionViewHolder(parent, onBindListener, itemActionListener)
            TravelDestinationArticleViewHolder.LAYOUT -> return TravelDestinationArticleViewHolder(parent, onBindListener, itemActionListener)
        }
        return super.createViewHolder(parent, type)
    }

    override fun type(viewModel: TravelDestinationSummaryModel): Int = TravelDestinationSummaryViewHolder.LAYOUT
    override fun type(model: TravelDestinationSectionModel): Int {
        return if (model.type == CITY_RECOMMENDATION_ORDER) TravelDestinationCityRecommendationViewHolder.LAYOUT
        else TravelDestinationSectionViewHolder.LAYOUT
    }

    override fun type(viewModel: TravelArticleModel): Int = TravelDestinationArticleViewHolder.LAYOUT
}