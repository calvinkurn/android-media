package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

interface TokoMartHomeTypeFactory {

    fun type(uiModel: HomeSectionUiModel): Int
}
