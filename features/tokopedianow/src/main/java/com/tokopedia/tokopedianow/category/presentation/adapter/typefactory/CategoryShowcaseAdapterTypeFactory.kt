package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryShowcaseTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener

class CategoryShowcaseAdapterTypeFactory(
    private var categoryShowcaseItemListener: CategoryShowcaseItemListener? = null,
    private val productCardCompactListener: ProductCardCompactView.ProductCardCompactListener? = null,
    private val productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener? = null,
    private val lifecycleOwner: LifecycleOwner? = null
): BaseAdapterTypeFactory(), CategoryShowcaseTypeFactory {

    override fun type(uiModel: CategoryShowcaseItemUiModel): Int = CategoryShowcaseItemViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CategoryShowcaseItemViewHolder.LAYOUT -> CategoryShowcaseItemViewHolder(
                itemView = view,
                listener = categoryShowcaseItemListener,
                productCardCompactListener = productCardCompactListener,
                productCardCompactSimilarProductTrackerListener = productCardCompactSimilarProductTrackerListener,
                lifecycleOwner = lifecycleOwner
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
