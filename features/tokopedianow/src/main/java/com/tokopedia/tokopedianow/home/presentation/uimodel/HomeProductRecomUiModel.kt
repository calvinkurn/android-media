package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

class HomeProductRecomUiModel(
    val id: String,
    val list: List<HomeProductCardUiModel>
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}