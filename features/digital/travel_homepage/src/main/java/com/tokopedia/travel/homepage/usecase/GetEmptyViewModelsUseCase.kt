package com.tokopedia.travel.homepage.usecase

import com.tokopedia.travel.homepage.data.*

/**
 * @author by jessica on 2019-08-19
 */

class GetEmptyViewModelsUseCase {

    fun requestEmptyViewModels(): List<TravelHomepageItemModel> {
        return listOf(TravelHomepageBannerModel(),
                TravelHomepageCategoryListModel(),
                TravelHomepageSectionViewModel(type = TravelHomepageSectionViewModel.TYPE_ORDER_LIST),
                TravelHomepageSectionViewModel(type = TravelHomepageSectionViewModel.TYPE_RECENT_SEARCH),
                TravelHomepageSectionViewModel(type = TravelHomepageSectionViewModel.TYPE_RECOMMENDATION),
                TravelHomepageDestinationModel())
    }
}