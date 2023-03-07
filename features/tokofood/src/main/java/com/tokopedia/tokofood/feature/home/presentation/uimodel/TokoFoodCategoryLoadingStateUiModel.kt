package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.TokoFoodCategoryTypeFactory

data class TokoFoodCategoryLoadingStateUiModel (
    val id: String
): Visitable<TokoFoodCategoryTypeFactory> {
    override fun type(typeFactory: TokoFoodCategoryTypeFactory): Int {
        return typeFactory.type(this)
    }
}