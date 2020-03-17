package com.tokopedia.travelhomepage.homepage.presentation.listener

/**
 * @author by jessica on 2019-08-12
 */

interface TravelHomepageActionListener {

    fun onItemClick(appUrl: String, webUrl: String = "")

    fun onViewSliderBanner()

    fun onClickSliderBannerItem()

    fun onClickSeeAllSliderBanner()

    fun onClickDynamicIcon()

    fun onClickDynamicBannerItem()

    fun onViewDynamicBanners()

    fun onViewProductCards()

    fun onClickProductCard()

    fun onClickSeeAllProductCards()

    fun onViewLegoBanner()

    fun onClickLegoBanner()

    fun onViewProductSlider()

    fun onClickProductSliderItem()

    fun onClickSeeAllProductSlider()

}