package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeCategoryMenuUiModel(
    val id: String,
    val title: String,
    val iconUrl: String
): HomeLayoutUiModel {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}