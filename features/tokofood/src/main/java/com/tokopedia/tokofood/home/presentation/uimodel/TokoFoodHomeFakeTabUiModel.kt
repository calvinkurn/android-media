package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeFakeTabUiModel (
    val id: String,
    val fakeTab: TokoFoodFakeTab?,
    @TokoFoodHomeLayoutState val state: Int
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