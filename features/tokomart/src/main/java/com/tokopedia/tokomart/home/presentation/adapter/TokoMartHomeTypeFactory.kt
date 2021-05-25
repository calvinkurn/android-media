package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryItemUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

interface TokoMartHomeTypeFactory {

    fun type(uiModel: HomeSectionUiModel): Int
    fun type(uiModel: HomeCategoryGridUiModel): Int
    fun type(uiModel: HomeCategoryItemUiModel): Int
}
