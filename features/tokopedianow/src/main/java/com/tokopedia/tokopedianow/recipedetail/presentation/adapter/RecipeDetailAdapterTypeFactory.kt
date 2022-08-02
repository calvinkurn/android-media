package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.LoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.LoadingViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.MediaSliderViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeInfoViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeTabViewHolder


class RecipeDetailAdapterTypeFactory(
    private val view: RecipeDetailView
): BaseAdapterTypeFactory(), RecipeDetailTypeFactory, RecipeLoadingTypeFactory {

    override fun type(uiModel: MediaSliderUiModel): Int = MediaSliderViewHolder.LAYOUT

    override fun type(uiModel: RecipeInfoUiModel): Int = RecipeInfoViewHolder.LAYOUT

    override fun type(uiModel: RecipeTabUiModel): Int = RecipeTabViewHolder.LAYOUT

    override fun type(uiModel: LoadingUiModel): Int = LoadingViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            MediaSliderViewHolder.LAYOUT -> MediaSliderViewHolder(parent)
            RecipeInfoViewHolder.LAYOUT -> RecipeInfoViewHolder(parent)
            RecipeTabViewHolder.LAYOUT -> RecipeTabViewHolder(parent, view)
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}