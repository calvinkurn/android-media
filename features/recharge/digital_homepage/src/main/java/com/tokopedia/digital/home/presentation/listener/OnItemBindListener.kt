package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel

interface OnItemBindListener {
    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
}