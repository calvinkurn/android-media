package com.tokopedia.travelhomepage.destination.model.mapper

import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.ORDER_LIST_ORDER
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel

/**
 * @author by jessica on 2019-08-14
 */

class TravelDestinationMapper {

    fun mapToSectionViewModel(model: TravelHomepageRecommendationModel,
                              type: Int): TravelDestinationSectionViewModel {
        val viewModel = TravelDestinationSectionViewModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.items.map {
            TravelDestinationSectionViewModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = type
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelHomepageOrderListModel): TravelDestinationSectionViewModel {
        val viewModel = TravelDestinationSectionViewModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.orders.map {
            TravelDestinationSectionViewModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = ORDER_LIST_ORDER
        return viewModel
    }
}