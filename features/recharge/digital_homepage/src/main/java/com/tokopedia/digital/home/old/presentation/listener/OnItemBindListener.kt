package com.tokopedia.digital.home.old.presentation.listener

import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.old.model.*

interface OnItemBindListener {
    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onBannerAllItemClicked()
    fun onSectionItemClicked(element: DigitalHomePageSectionModel.Item, position: Int, sectionType: String)
    fun onRecommendationClicked(element: RecommendationItemEntity, position: Int)

    fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onSectionItemImpression(elements: List<DigitalHomePageSectionModel.Item>, sectionType: String)
    fun onRecommendationImpression(elements: List<RecommendationItemEntity>)
}