package com.tokopedia.travelhomepage.destination.usecase

import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationItemModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_DEALS_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_EVENT_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_RECOMMENDATION_ORDER
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.ORDER_LIST_ORDER

/**
 * @author by jessica on 2020-01-02
 */

class GetEmptyModelsUseCase {

    fun requestEmptyViewModels(): List<TravelDestinationItemModel> {
        return listOf(TravelDestinationSummaryModel(),
                TravelDestinationSectionModel(type = ORDER_LIST_ORDER),
                TravelDestinationSectionModel(type = CITY_RECOMMENDATION_ORDER),
                TravelDestinationSectionModel(type = CITY_EVENT_ORDER),
                TravelDestinationSectionModel(type = CITY_DEALS_ORDER),
                TravelArticleModel())
    }
}