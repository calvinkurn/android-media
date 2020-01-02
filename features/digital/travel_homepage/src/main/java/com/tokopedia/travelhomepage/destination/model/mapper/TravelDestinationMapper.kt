package com.tokopedia.travelhomepage.destination.model.mapper

import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.CITY_RECOMMENDATION_ORDER
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

/**
 * @author by jessica on 2019-08-14
 */

class TravelDestinationMapper {

    fun mapToSectionViewModel(model: TravelHomepageRecommendationModel,
                              type: Int): TravelDestinationSectionViewModel {
        val viewModel = TravelDestinationSectionViewModel()
        viewModel.title = model.meta.title
        viewModel.seeAllUrl = model.meta.appUrl
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
}