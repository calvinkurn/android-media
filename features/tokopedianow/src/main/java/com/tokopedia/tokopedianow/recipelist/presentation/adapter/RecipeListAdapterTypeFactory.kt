package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorAnalytics
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeCountUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeEmptyStateUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeFilterViewHolder.RecipeChipFilterListener
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeCountViewHolder
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeFilterViewHolder
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeHeaderViewHolder
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeViewHolder
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeEmptyStateViewHolder
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeViewHolder.RecipeItemListener
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeEmptyStateViewHolder.RecipeEmptyStateListener

class RecipeListAdapterTypeFactory(
    private val recipeItemListener: RecipeItemListener,
    private val recipeFilterListener: RecipeChipFilterListener,
    private val serverErrorListener: ServerErrorListener,
    private val serverErrorAnalytics: ServerErrorAnalytics,
    private val recipeEmptyStateListener: RecipeEmptyStateListener
) : BaseAdapterTypeFactory(), RecipeListTypeFactory, TokoNowServerErrorTypeFactory {

    override fun type(uiModel: RecipeHeaderUiModel): Int = RecipeHeaderViewHolder.LAYOUT

    override fun type(uiModel: RecipeCountUiModel): Int = RecipeCountViewHolder.LAYOUT

    override fun type(uiModel: RecipeUiModel): Int = RecipeViewHolder.LAYOUT

    override fun type(uiModel: RecipeFilterUiModel): Int = RecipeFilterViewHolder.LAYOUT

    override fun type(uiModel: RecipeEmptyStateUiModel): Int = RecipeEmptyStateViewHolder.LAYOUT

    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT

    override fun type(viewModel: LoadingMoreModel?): Int = TokoNowLoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RecipeHeaderViewHolder.LAYOUT -> RecipeHeaderViewHolder(parent)
            RecipeCountViewHolder.LAYOUT -> RecipeCountViewHolder(parent)
            RecipeViewHolder.LAYOUT -> RecipeViewHolder(parent, recipeItemListener)
            RecipeFilterViewHolder.LAYOUT -> RecipeFilterViewHolder(parent, recipeFilterListener)
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(parent, serverErrorListener, serverErrorAnalytics)
            RecipeEmptyStateViewHolder.LAYOUT -> RecipeEmptyStateViewHolder(parent, recipeEmptyStateListener)
            TokoNowLoadingMoreViewHolder.LAYOUT -> TokoNowLoadingMoreViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}