package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodCategoryTypeFactory

class TokoFoodCategoryEmptyStateUiModel: Visitable<TokoFoodCategoryTypeFactory> {
    override fun type(typeFactory: TokoFoodCategoryTypeFactory): Int {
        return typeFactory.type(this)
    }
}
