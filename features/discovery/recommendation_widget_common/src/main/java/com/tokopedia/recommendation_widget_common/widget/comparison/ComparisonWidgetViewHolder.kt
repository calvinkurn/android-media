package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import kotlinx.android.synthetic.main.view_holder_comparison_widget.view.*

class ComparisonWidgetViewHolder(val view: View): AbstractViewHolder<ComparisonWidgetDataModel>(view) {
    val context = view.context
    override fun bind(element: ComparisonWidgetDataModel) {
        view.comparison_widget.setComparisonWidgetData(element.comparisonListModel)
    }

    companion object{
        val LAYOUT = R.layout.best_seller_view_holder
    }
}