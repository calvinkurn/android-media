package com.tokopedia.travel.homepage.usecase

import com.tokopedia.travel.homepage.data.*
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

/**
 * @author by jessica on 2019-08-19
 */

class GetEmptyViewModelsUseCase {

    fun requestEmptyViewModels(): List<TravelHomepageItemModel> {
        return listOf(TravelHomepageBannerModel(),
                TravelHomepageCategoryListModel(),
                TravelHomepageSectionViewModel(type = TYPE_ORDER_LIST),
                TravelHomepageSectionViewModel(type = TYPE_RECENT_SEARCH),
                TravelHomepageSectionViewModel(type = TYPE_RECOMMENDATION),
                TravelHomepageDestinationModel())
    }
}