package com.tokopedia.travelhomepage.homepage.data.mapper

import com.tokopedia.travelhomepage.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecentSearchModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionViewModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECENT_SEARCH
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_RECOMMENDATION

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
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_ORDER_LIST
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
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_RECENT_SEARCH
        viewModel.categoryType = model.meta.type
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
                    appUrl = it.appUrl, imageUrl = it.imageUrl, product = it.product
            )
        }
        viewModel.type = TYPE_RECOMMENDATION
        return viewModel
    }
}