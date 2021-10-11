package com.tokopedia.tokopedianow.common.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryItemUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryItemViewHolder

class TokoNowCategoryGridAdapterTypeFactory(
    private val tokoNowCategoryItemListener: TokoNowCategoryItemViewHolder.TokoNowCategoryItemListener? = null,
):BaseAdapterTypeFactory(), TokoNowItemTypeFactory {
    override fun type(uiModel: TokoNowCategoryItemUiModel): Int = TokoNowCategoryItemViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TokoNowCategoryItemViewHolder.LAYOUT -> TokoNowCategoryItemViewHolder(view, tokoNowCategoryItemListener)
            else -> super.createViewHolder(view, type)
        }
    }
}