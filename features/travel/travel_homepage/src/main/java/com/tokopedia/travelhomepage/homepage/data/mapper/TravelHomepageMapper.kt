package com.tokopedia.travelhomepage.homepage.data.mapper

import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

/**
 * @author by jessica on 2019-08-14
 */

class TravelHomepageMapper {

    fun mapToSectionViewModel(model: TravelHomepageOrderListModel): TravelHomepageSectionModel {
        val viewModel = TravelHomepageSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.orders.map {
            TravelHomepageSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_ORDER_LIST
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelRecentSearchModel): TravelHomepageSectionModel {
        val viewModel = TravelHomepageSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.items.map {
            TravelHomepageSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_RECENT_SEARCH
        viewModel.categoryType = model.travelMeta.type
        return viewModel
    }

    fun mapToSectionViewModel(model: TravelHomepageRecommendationModel): TravelHomepageSectionModel {
        val viewModel = TravelHomepageSectionModel()
        viewModel.title = model.travelMeta.title
        viewModel.seeAllUrl = model.travelMeta.appUrl
        viewModel.list = model.items.map {
            TravelHomepageSectionModel.Item(title = it.title,
                    subtitle = it.subtitle, prefix = it.prefix,
                    prefixStyling = it.prefixStyling, value = it.value,
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_RECOMMENDATION
        return viewModel
    }
}