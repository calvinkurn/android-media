package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryNavigationItemTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationItemViewHolder

class CategoryNavigationAdapterTypeFactory(
    private val categoryNavigationItemListener: CategoryNavigationItemViewHolder.CategoryNavigationItemListener? = null,
):BaseAdapterTypeFactory(), CategoryNavigationItemTypeFactory {
    override fun type(uiModel: CategoryNavigationItemUiModel): Int = CategoryNavigationItemViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CategoryNavigationItemViewHolder.LAYOUT -> CategoryNavigationItemViewHolder(
                itemView = view,
                listener = categoryNavigationItemListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
