package com.tokopedia.travelhomepage.destination.model.mapper

import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.model.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.destination.model.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.ORDER_LIST_ORDER

/**
 * @author by jessica on 2019-08-14
 */

class TravelDestinationMapper {

    fun mapToSectionViewModel(model: TravelHomepageRecommendationModel,
                              type: Int): TravelDestinationSectionModel {
        val viewModel = TravelDestinationSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.items.map {
            TravelDestinationSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = type
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelHomepageOrderListModel): TravelDestinationSectionModel {
        val viewModel = TravelDestinationSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.orders.map {
            TravelDestinationSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = ORDER_LIST_ORDER
        return viewModel
    }
}