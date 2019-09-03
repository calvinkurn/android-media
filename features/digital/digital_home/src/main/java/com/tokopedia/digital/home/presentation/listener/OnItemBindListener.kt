package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel

interface OnItemBindListener {
    fun onBannerItemDigitalBind()
    fun onCategoryItemDigitalBind()
    fun onPromoItemDigitalBind()
    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?)
    fun onBannerItemClicked(element : DigitalHomePageBannerModel.Banner?)
    fun onBannerAllItemClicked()
}