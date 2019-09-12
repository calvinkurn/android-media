package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel

interface OnItemBindListener {
    fun onBannerItemDigitalBind(loadFromCloud: Boolean?)
    fun onCategoryItemDigitalBind(loadFromCloud: Boolean?)
    fun onPromoItemDigitalBind()
    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, i: Int)
    fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onBannerAllItemClicked()
    fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, i: Int)
    fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
}