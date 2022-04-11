package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodFakeTabUiModel (
    val id: String,
    val fakeTab: TokoFoodFakeTab
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokoFoodFakeTab(
    val id: String = "",
    val imgNyam: String = "",
    val applinkNyam: String = "",
    val imgTokoFood: String = "",
    val applinkTokoFood: String = ""
)