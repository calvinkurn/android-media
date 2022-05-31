package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodCommonTypeFactory

data class TokoFoodProgressBarUiModel(
    val id: String
): Visitable<TokoFoodCommonTypeFactory> {
    override fun type(typeFactory: TokoFoodCommonTypeFactory): Int {
        return typeFactory.type(this)
    }
}
