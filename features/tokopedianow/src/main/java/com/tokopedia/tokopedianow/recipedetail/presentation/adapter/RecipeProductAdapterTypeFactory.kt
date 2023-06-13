package com.tokopedia.tokopedianow.recipedetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.recipedetail.analytics.ProductAnalytics
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener

class RecipeProductAdapterTypeFactory(
    private val productListener: RecipeProductListener?,
    private val productAnalytics: ProductAnalytics?
) : BaseAdapterTypeFactory(), RecipeProductTypeFactory {

    override fun type(uiModel: RecipeProductUiModel): Int = RecipeProductViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RecipeProductViewHolder.LAYOUT -> RecipeProductViewHolder(parent, productListener, productAnalytics)
            else -> super.createViewHolder(parent, type)
        }
    }
}