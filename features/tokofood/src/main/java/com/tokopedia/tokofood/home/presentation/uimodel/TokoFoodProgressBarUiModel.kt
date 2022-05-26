package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodProgressBarTypeFactory

data class TokoFoodProgressBarUiModel(
    val id: String
): Visitable<TokoFoodProgressBarTypeFactory> {
    override fun type(typeFactory: TokoFoodProgressBarTypeFactory): Int {
        return typeFactory.type(this)
    }
}
