package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeLoadingStateUiModel (
    val id: String
    ): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}