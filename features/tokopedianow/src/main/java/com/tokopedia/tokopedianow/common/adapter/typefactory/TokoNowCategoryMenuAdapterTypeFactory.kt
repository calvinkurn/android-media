package com.tokopedia.tokopedianow.common.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemSeeAllUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuItemSeeAllViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuItemViewHolder

class TokoNowCategoryMenuAdapterTypeFactory(
    private val tokoNowCategoryMenuItemListener: TokoNowCategoryMenuItemViewHolder.TokoNowCategoryMenuItemListener? = null,
    private val tokoNowCategoryMenuItemSeeAllListener: TokoNowCategoryMenuItemSeeAllViewHolder.TokoNowCategoryMenuItemSeeAllListener? = null
):BaseAdapterTypeFactory(), TokoNowCategoryMenuItemTypeFactory {
    override fun type(uiModel: TokoNowCategoryMenuItemUiModel): Int = TokoNowCategoryMenuItemViewHolder.LAYOUT
    override fun type(uiModel: TokoNowCategoryMenuItemSeeAllUiModel): Int = TokoNowCategoryMenuItemSeeAllViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TokoNowCategoryMenuItemViewHolder.LAYOUT -> TokoNowCategoryMenuItemViewHolder(
                itemView = view,
                listener = tokoNowCategoryMenuItemListener
            )
            TokoNowCategoryMenuItemSeeAllViewHolder.LAYOUT -> TokoNowCategoryMenuItemSeeAllViewHolder(
                itemView = view,
                listener = tokoNowCategoryMenuItemSeeAllListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
