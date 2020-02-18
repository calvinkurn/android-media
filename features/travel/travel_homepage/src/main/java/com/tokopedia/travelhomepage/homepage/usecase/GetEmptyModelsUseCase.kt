package com.tokopedia.travelhomepage.homepage.usecase

import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

/**
 * @author by jessica on 2019-08-19
 */

class GetEmptyModelsUseCase {

    fun requestEmptyViewModels(loadFromCloud: Boolean): List<TravelHomepageItemModel> {

        val travelHomepageBannerModel = TravelHomepageBannerModel()
        travelHomepageBannerModel.isLoadFromCloud = loadFromCloud

        val categoryListModel = TravelHomepageCategoryListModel()
        categoryListModel.isLoadFromCloud = loadFromCloud

        val orderListModel = TravelHomepageSectionModel(type = TYPE_ORDER_LIST)
        orderListModel.isLoadFromCloud = loadFromCloud

        val recentSearchModel = TravelHomepageSectionModel(type = TYPE_RECENT_SEARCH)
        recentSearchModel.isLoadFromCloud = loadFromCloud

        val recommendationModel = TravelHomepageSectionModel(type = TYPE_RECOMMENDATION)
        recommendationModel.isLoadFromCloud = loadFromCloud

        val destinationModel = TravelHomepageDestinationModel()
        destinationModel.isLoadFromCloud = loadFromCloud

        return listOf(travelHomepageBannerModel,
                categoryListModel,
                orderListModel,
                recentSearchModel,
                recommendationModel,
                destinationModel)
    }
}