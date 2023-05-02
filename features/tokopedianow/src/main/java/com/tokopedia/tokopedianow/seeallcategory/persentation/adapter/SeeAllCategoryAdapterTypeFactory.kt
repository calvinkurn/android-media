package com.tokopedia.tokopedianow.seeallcategory.persentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.seeallcategory.persentation.uimodel.SeeAllCategoryItemUiModel
import com.tokopedia.tokopedianow.seeallcategory.persentation.viewholder.SeeAllCategoryItemViewHolder

class SeeAllCategoryAdapterTypeFactory(
    private val seeAllCategoryListener: SeeAllCategoryItemViewHolder.SeeAllCategoryListener
): BaseAdapterTypeFactory(), SeeAllCategoryTypeFactory {
    override fun type(uiModel: SeeAllCategoryItemUiModel): Int = SeeAllCategoryItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SeeAllCategoryItemViewHolder.LAYOUT -> SeeAllCategoryItemViewHolder(
                itemView = parent,
                listener = seeAllCategoryListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
