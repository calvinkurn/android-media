package com.tokopedia.travelhomepage.homepage.presentation.listener

import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionViewModel

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemClickListener {

    fun onTrackBannerImpression(banner: TravelHomepageBannerModel.Banner, position: Int)

    fun onTrackBannerClick(banner: TravelHomepageBannerModel.Banner, position: Int)

    fun onTrackCategoryClick(category: TravelHomepageCategoryListModel.Category, position: Int)

    fun onTrackDealsClick(deal: TravelHomepageSectionViewModel.Item, position: Int)

    fun onTrackPopularDestinationClick(destination: TravelHomepageDestinationModel.Destination, position: Int)

    fun onTrackEventClick(type: Int, position: Int = 0, categoryName: String = "")

    fun onItemClick(appUrl: String, webUrl: String = "")

}