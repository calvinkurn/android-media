package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import kotlinx.android.synthetic.main.item_comparison_widget.view.*

class ComparisonWidgetAdapter(var comparisonListModel: ComparisonListModel): RecyclerView.Adapter<ComparisonWidgetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparisonWidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comparison_widget, parent, false)

        val productCardView = view.productCardView
        val layoutParams = productCardView.layoutParams
        layoutParams.height = comparisonListModel.comparisonWidgetConfig.productCardHeight
        productCardView.layoutParams = layoutParams

        return ComparisonWidgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comparisonListModel.comparisonData.size
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ComparisonWidgetViewHolder, position: Int) {
        if (position < comparisonListModel.comparisonData.size) {
            holder.bind(comparisonListModel.comparisonData[position])
        }
    }
}