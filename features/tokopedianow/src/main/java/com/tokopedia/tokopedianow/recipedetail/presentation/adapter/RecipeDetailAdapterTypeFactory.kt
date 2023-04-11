package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.analytics.MediaSliderAnalytics
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder.TagViewHolder.TagListener
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeDetailLoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.view.RecipeDetailView
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeDetailLoadingViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.MediaSliderViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeInfoViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeTabViewHolder

class RecipeDetailAdapterTypeFactory(
    private val view: RecipeDetailView,
    private val tagListener: TagListener,
    private val serverErrorListener: ServerErrorListener,
    private val mediaSliderAnalytics: MediaSliderAnalytics
): BaseAdapterTypeFactory(), RecipeDetailTypeFactory, TokoNowServerErrorTypeFactory {

    override fun type(uiModel: MediaSliderUiModel): Int = MediaSliderViewHolder.LAYOUT

    override fun type(uiModel: RecipeInfoUiModel): Int = RecipeInfoViewHolder.LAYOUT

    override fun type(uiModel: RecipeTabUiModel): Int = RecipeTabViewHolder.LAYOUT

    override fun type(uiModel: RecipeDetailLoadingUiModel): Int = RecipeDetailLoadingViewHolder.LAYOUT

    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            MediaSliderViewHolder.LAYOUT -> MediaSliderViewHolder(parent, mediaSliderAnalytics)
            RecipeInfoViewHolder.LAYOUT -> RecipeInfoViewHolder(parent, tagListener)
            RecipeTabViewHolder.LAYOUT -> RecipeTabViewHolder(parent, view)
            RecipeDetailLoadingViewHolder.LAYOUT -> RecipeDetailLoadingViewHolder(parent)
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(parent, serverErrorListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}