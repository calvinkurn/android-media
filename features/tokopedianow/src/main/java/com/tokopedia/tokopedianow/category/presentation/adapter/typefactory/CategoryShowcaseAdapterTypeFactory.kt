package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryShowcaseTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder

class CategoryShowcaseAdapterTypeFactory(
): BaseAdapterTypeFactory(), CategoryShowcaseTypeFactory {

    override fun type(uiModel: CategoryShowcaseItemUiModel): Int = CategoryShowcaseItemViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CategoryShowcaseItemViewHolder.LAYOUT -> CategoryShowcaseItemViewHolder(
                itemView = view
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
