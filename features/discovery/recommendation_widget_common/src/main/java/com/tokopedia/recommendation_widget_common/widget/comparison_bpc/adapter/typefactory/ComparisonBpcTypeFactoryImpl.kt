package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.ComparisonBpcSeeMoreViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcItemModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.ComparisonBpcWidgetItemViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking.ComparisonBpcAnalyticListener

/**
 * Created by Frenzel
 */
class ComparisonBpcTypeFactoryImpl(
    private val listener: ComparisonBpcAnalyticListener
) : ComparisonBpcTypeFactory {
    override fun type(dataModel: ComparisonBpcSeeMoreDataModel): Int {
        return ComparisonBpcSeeMoreViewHolder.LAYOUT
    }

    override fun type(dataModel: ComparisonBpcItemModel): Int {
        return ComparisonBpcWidgetItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<Visitable<ComparisonBpcTypeFactory>> {
        return when (viewType) {
            ComparisonBpcSeeMoreViewHolder.LAYOUT -> ComparisonBpcSeeMoreViewHolder(view, listener)
            ComparisonBpcWidgetItemViewHolder.LAYOUT -> ComparisonBpcWidgetItemViewHolder(view, listener)
            else -> throw Exception("The type layout not supported")
        } as AbstractViewHolder<Visitable<ComparisonBpcTypeFactory>>
    }
}
