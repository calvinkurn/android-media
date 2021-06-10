package com.tokopedia.tokomart.categorylist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder
import com.tokopedia.tokomart.categorylist.presentation.viewholder.CategoryListItemViewHolder.CategoryListListener

class TokoMartCategoryListAdapterTypeFactory(
    private val categoryListListener: CategoryListListener,
    private val parentLvl: String
) : BaseAdapterTypeFactory(), TokoMartCategoryListTypeFactory {

    override fun type(uiModel: CategoryListChildUiModel): Int = CategoryListItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CategoryListItemViewHolder.LAYOUT -> CategoryListItemViewHolder(parent, categoryListListener, parentLvl)
            else -> super.createViewHolder(parent, type)
        }
    }
}