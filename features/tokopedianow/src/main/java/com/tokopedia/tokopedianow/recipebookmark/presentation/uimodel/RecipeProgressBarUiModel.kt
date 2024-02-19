package com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.oldrecipebookmark.presentation.adapter.RecipeBookmarkTypeFactory

class RecipeProgressBarUiModel: Visitable<RecipeBookmarkTypeFactory> {
    override fun type(typeFactory: RecipeBookmarkTypeFactory): Int = typeFactory.type(this)
}
