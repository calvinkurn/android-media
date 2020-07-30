package com.tokopedia.travelhomepage.homepage.presentation.listener

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel

/**
 * @author by jessica on 2019-08-12
 */

interface TravelHomepageActionListener {

    fun onItemClick(appUrl: String, webUrl: String = "")

    fun onViewSliderBanner(banner: TravelCollectiveBannerModel.Banner, position: Int)

    fun onClickSliderBannerItem(banner: TravelCollectiveBannerModel.Banner, position: Int)

    fun onClickSeeAllSliderBanner()

    fun onClickDynamicIcon(category: TravelHomepageCategoryListModel.Category, position: Int)

    fun onClickDynamicBannerItem(destination: TravelHomepageDestinationModel.Destination, position: Int, componentPosition: Int, sectionTitle: String)

    fun onViewDynamicBanners(destination: List<TravelHomepageDestinationModel.Destination>, componentPosition: Int, sectionTitle: String)

    fun onViewProductCards(list: List<ProductGridCardItemModel>, componentPosition: Int, sectionTitle: String)

    fun onClickProductCard(item: ProductGridCardItemModel, position: Int, componentPosition: Int, sectionTitle: String)

    fun onClickSeeAllProductCards(componentPosition: Int, sectionTitle: String)

    fun onViewLegoBanner(list: List<LegoBannerItemModel>, componentPosition: Int, sectionTitle: String)

    fun onClickLegoBanner(item: LegoBannerItemModel, position: Int, componentPosition: Int, sectionTitle: String)

    fun onViewProductSlider(list: List<TravelHomepageSectionModel.Item>, componentPosition: Int, sectionTitle: String)

    fun onClickProductSliderItem(item: TravelHomepageSectionModel.Item, position: Int, componentPosition: Int, sectionTitle: String)

    fun onClickSeeAllProductSlider(componentPosition: Int, sectionTitle: String)

}