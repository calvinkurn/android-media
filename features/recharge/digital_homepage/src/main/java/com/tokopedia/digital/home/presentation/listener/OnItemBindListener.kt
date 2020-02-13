package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageSectionModel

interface OnItemBindListener {
    fun onBannerItemDigitalBind(loadFromCloud: Boolean?)
    fun onCategoryItemDigitalBind(loadFromCloud: Boolean?)
    fun onFavoritesItemDigitalBind(loadFromCloud: Boolean?)
    fun onTrustMarkItemDigitalBind(loadFromCloud: Boolean?)
    fun onNewUserZoneItemDigitalBind(loadFromCloud: Boolean?)
    fun onSpotlightItemDigitalBind(loadFromCloud: Boolean?)
    fun onSubscriptionItemDigitalBind(loadFromCloud: Boolean?)
    fun onRecommendationItemDigitalBind(loadFromCloud: Boolean?)
    fun onPromoItemDigitalBind()

    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, i: Int)
    fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onBannerAllItemClicked()
    fun onSectionItemClicked(element: DigitalHomePageSectionModel.Item, i: Int, sectionType: String)
    fun onRecommendationClicked(element: RecommendationItemEntity, position: Int)

    fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, i: Int)
    fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onSectionItemImpression(elements: List<DigitalHomePageSectionModel.Item>, sectionType: String, initial: Boolean = false)
    fun onRecommendationImpression(elements: List<RecommendationItemEntity>, initial: Boolean = false)
}