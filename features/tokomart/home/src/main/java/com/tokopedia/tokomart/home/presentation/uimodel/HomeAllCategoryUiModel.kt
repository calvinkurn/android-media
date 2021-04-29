package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.base.uimodel.BaseHomeUiModel
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeAllCategoryUiModel(
    val id: String,
    val title: String
): BaseHomeUiModel {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}