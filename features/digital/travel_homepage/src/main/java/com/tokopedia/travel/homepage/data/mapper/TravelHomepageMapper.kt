package com.tokopedia.travel.homepage.data.mapper

import com.tokopedia.travel.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travel.homepage.data.TravelHomepageRecentSearchModel
import com.tokopedia.travel.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel

/**
 * @author by jessica on 2019-08-14
 */

class TravelHomepageMapper {

    fun mapToSectionViewModel(model: TravelHomepageOrderListModel): TravelHomepageSectionViewModel {
        val viewModel = TravelHomepageSectionViewModel()
        viewModel.title = model.meta.title
        viewModel.seeAllUrl = model.meta.appUrl
        viewModel.list = model.orders.map {
            TravelHomepageSectionViewModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl
                    )
        }
        viewModel.type = TravelHomepageSectionViewModel.TYPE_ORDER_LIST
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelHomepageRecentSearchModel): TravelHomepageSectionViewModel {
        val viewModel = TravelHomepageSectionViewModel()
        viewModel.title = model.meta.title
        viewModel.seeAllUrl = model.meta.appUrl
        viewModel.list = model.items.map {
            TravelHomepageSectionViewModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl
                    )
        }
        viewModel.type = TravelHomepageSectionViewModel.TYPE_RECENT_SEARCH
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelHomepageRecommendationModel): TravelHomepageSectionViewModel {
        val viewModel = TravelHomepageSectionViewModel()
        viewModel.title = model.meta.title
        viewModel.seeAllUrl = model.meta.appUrl
        viewModel.list = model.items.map {
            TravelHomepageSectionViewModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl
            )
        }
        viewModel.type = TravelHomepageSectionViewModel.TYPE_RECOMMENDATION
        return viewModel
    }
}