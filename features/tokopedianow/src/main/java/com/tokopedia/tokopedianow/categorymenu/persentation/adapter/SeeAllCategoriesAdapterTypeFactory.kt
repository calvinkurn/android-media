package com.tokopedia.tokopedianow.categorymenu.persentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.categorymenu.persentation.uimodel.SeeAllCategoriesItemUiModel
import com.tokopedia.tokopedianow.categorymenu.persentation.viewholder.SeeAllCategoriesItemViewHolder

class SeeAllCategoriesAdapterTypeFactory: BaseAdapterTypeFactory(), SeeAllCategoriesTypeFactory {
    override fun type(uiModel: SeeAllCategoriesItemUiModel): Int = SeeAllCategoriesItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SeeAllCategoriesItemViewHolder.LAYOUT -> SeeAllCategoriesItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
