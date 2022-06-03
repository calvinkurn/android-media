package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodCommonTypeFactory

class TokoFoodErrorStateUiModel(
    val id: String,
    val throwable: Throwable
): Visitable<TokoFoodCommonTypeFactory> {
    override fun type(typeFactory: TokoFoodCommonTypeFactory): Int {
        return typeFactory.type(this)
    }
}