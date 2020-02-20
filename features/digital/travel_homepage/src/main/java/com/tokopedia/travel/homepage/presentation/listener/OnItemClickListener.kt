package com.tokopedia.travel.homepage.presentation.listener

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travel.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travel.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemClickListener {

    fun onTrackBannerImpression(banner: TravelCollectiveBannerModel.Banner, position: Int)

    fun onTrackBannerClick(banner: TravelCollectiveBannerModel.Banner, position: Int)

    fun onTrackCategoryClick(category: TravelHomepageCategoryListModel.Category, position: Int)

    fun onTrackDealsClick(deal: TravelHomepageSectionViewModel.Item, position: Int)

    fun onTrackPopularDestinationClick(destination: TravelHomepageDestinationModel.Destination, position: Int)

    fun onTrackEventClick(type: Int, position: Int = 0, categoryName: String = "")

    fun onItemClick(appUrl: String, webUrl: String = "")

}