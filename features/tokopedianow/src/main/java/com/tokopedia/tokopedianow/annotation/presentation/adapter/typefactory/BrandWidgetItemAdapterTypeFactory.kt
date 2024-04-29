package com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.annotation.analytic.AnnotationWidgetAnalytic
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetSeeAllUiModel
import com.tokopedia.tokopedianow.annotation.presentation.viewholder.BrandWidgetItemViewHolder
import com.tokopedia.tokopedianow.annotation.presentation.viewholder.BrandWidgetSeeAllViewHolder

class BrandWidgetItemAdapterTypeFactory(
    private val analytic: AnnotationWidgetAnalytic
): BaseAdapterTypeFactory(), BrandWidgetItemTypeFactory {

    override fun type(uiModel: BrandWidgetItemUiModel): Int = BrandWidgetItemViewHolder.LAYOUT

    override fun type(uiModel: BrandWidgetSeeAllUiModel): Int = BrandWidgetSeeAllViewHolder.LAYOUT

    override fun createViewHolder(itemView: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BrandWidgetItemViewHolder.LAYOUT -> BrandWidgetItemViewHolder(itemView, analytic)
            BrandWidgetSeeAllViewHolder.LAYOUT -> BrandWidgetSeeAllViewHolder(itemView, analytic)
            else -> super.createViewHolder(itemView, type)
        }
    }
}
