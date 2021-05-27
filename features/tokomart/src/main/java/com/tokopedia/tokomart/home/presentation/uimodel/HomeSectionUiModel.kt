package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeSectionUiModel(
    val id: String,
    val title: String
): TokoMartHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
