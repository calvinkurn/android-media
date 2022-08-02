package com.tokopedia.tokopedianow.recipedetail.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipedetail.presentation.adapter.RecipeLoadingTypeFactory

object LoadingUiModel: Visitable<RecipeLoadingTypeFactory> {
    override fun type(typeFactory: RecipeLoadingTypeFactory): Int {
        return typeFactory.type(this)
    }
}