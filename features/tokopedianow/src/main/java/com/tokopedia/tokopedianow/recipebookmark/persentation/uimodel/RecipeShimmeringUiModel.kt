package com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.RecipeBookmarkTypeFactory

class RecipeShimmeringUiModel: Visitable<RecipeBookmarkTypeFactory> {
    override fun type(typeFactory: RecipeBookmarkTypeFactory): Int = typeFactory.type(this)
}