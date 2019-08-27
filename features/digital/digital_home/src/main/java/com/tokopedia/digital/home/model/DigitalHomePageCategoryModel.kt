package com.tokopedia.digital.home.model

import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageItemModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory

class DigitalHomePageCategoryModel : DigitalHomePageItemModel() {
    override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
        return typeFactory.type(this)
    }
}