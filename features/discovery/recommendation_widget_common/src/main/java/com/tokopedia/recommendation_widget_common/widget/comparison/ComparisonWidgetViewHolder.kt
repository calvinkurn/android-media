package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comparison_widget.view.*

class ComparisonWidgetViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val context = view.context

    fun bind(comparisonModel: ComparisonModel) {
        view.specsView.setSpecsInfo(comparisonModel.specsModel)
    }
}