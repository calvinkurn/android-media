package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCommonTypeFactory

data class TokoFoodProgressBarUiModel(
    val id: String
): Visitable<TokoFoodCommonTypeFactory> {
    override fun type(typeFactory: TokoFoodCommonTypeFactory): Int {
        return typeFactory.type(this)
    }
}
