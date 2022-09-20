package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BuyAllProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.BuyAllProductViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.OutOfCoverageViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.OutOfCoverageViewHolder.OutOfCoverageListener
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener

class RecipeIngredientAdapterTypeFactory(
    private val productListener: RecipeProductListener?,
    private val outOfCoverageListener: OutOfCoverageListener?,
    private val productAnalytics: RecipeProductAnalytics?
) : BaseAdapterTypeFactory(), RecipeIngredientTypeFactory, RecipeProductTypeFactory {

    override fun type(uiModel: BuyAllProductUiModel): Int = BuyAllProductViewHolder.LAYOUT

    override fun type(uiModel: RecipeProductUiModel): Int = RecipeProductViewHolder.LAYOUT

    override fun type(uiModel: OutOfCoverageUiModel): Int = OutOfCoverageViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BuyAllProductViewHolder.LAYOUT -> BuyAllProductViewHolder(parent)
            RecipeProductViewHolder.LAYOUT -> RecipeProductViewHolder(parent, productListener, productAnalytics)
            OutOfCoverageViewHolder.LAYOUT -> OutOfCoverageViewHolder(parent, outOfCoverageListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}