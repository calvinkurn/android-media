package com.tokopedia.tokomart.category.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.category.presentation.typefactory.CategoryTypeFactory
import com.tokopedia.tokomart.category.presentation.viewholder.CategoryIsleViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryTypeFactoryImpl

class CategoryTypeFactoryImpl: BaseSearchCategoryTypeFactoryImpl(), CategoryTypeFactory {

    override fun type(categoryIsleDataView: CategoryIsleDataView) = CategoryIsleViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CategoryIsleViewHolder.LAYOUT -> CategoryIsleViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}