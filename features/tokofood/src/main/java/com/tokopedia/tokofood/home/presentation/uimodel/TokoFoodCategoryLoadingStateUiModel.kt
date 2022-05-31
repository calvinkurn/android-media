package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodCategoryTypeFactory

data class TokoFoodCategoryLoadingStateUiModel (
    val id: String
): Visitable<TokoFoodCategoryTypeFactory> {
    override fun type(typeFactory: TokoFoodCategoryTypeFactory): Int {
        return typeFactory.type(this)
    }
}