package com.tokopedia.tokomart.categorylist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListChildViewHolder

class TokoMartCategoryListAdapterTypeFactory : BaseAdapterTypeFactory(), TokoMartCategoryListTypeFactory {

    override fun type(uiModel: CategoryListItemUiModel): Int = CategoryListItemViewHolder.LAYOUT
    override fun type(uiModel: CategoryListChildUiModel): Int = CategoryListChildViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CategoryListItemViewHolder.LAYOUT -> CategoryListItemViewHolder(parent)
            CategoryListChildViewHolder.LAYOUT -> CategoryListChildViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}