package com.tokopedia.travel.homepage.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.travel.homepage.data.*

/**
 * @author by furqan on 06/08/2019
 */
interface TravelHomepageTypeFactory : AdapterTypeFactory {

    fun type(viewModel: TravelHomepageBannerModel): Int

    fun type(viewModel: TravelHomepageCategoryListModel): Int

    fun type(viewModel: TravelHomepageDestinationModel): Int

    fun type(viewModel: TravelHomepageOrderListModel): Int

    fun type(viewModel: TravelHomepageRecentSearchModel): Int

    fun type(viewModel: TravelHomepageRecommendationModel): Int

}