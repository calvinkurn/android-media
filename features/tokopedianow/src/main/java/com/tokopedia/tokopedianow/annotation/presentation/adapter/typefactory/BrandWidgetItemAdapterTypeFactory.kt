package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.viewholder.BrandWidgetItemViewHolder

class BrandWidgetItemAdapterTypeFactory: BaseAdapterTypeFactory(), BrandWidgetItemTypeFactory {

    override fun type(uiModel: BrandWidgetItemUiModel): Int = BrandWidgetItemViewHolder.LAYOUT

    override fun createViewHolder(itemView: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BrandWidgetItemViewHolder.LAYOUT -> BrandWidgetItemViewHolder(itemView)
            else -> super.createViewHolder(itemView, type)
        }
    }
}
