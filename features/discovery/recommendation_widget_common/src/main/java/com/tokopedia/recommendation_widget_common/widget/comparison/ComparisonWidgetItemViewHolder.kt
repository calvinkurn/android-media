package com.tokopedia.recommendation_widget_common.widget.comparison

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comparison_widget.view.*

class ComparisonWidgetItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val context = view.context

    fun bind(comparisonModel: ComparisonModel) {
        view.specsView.setSpecsInfo(comparisonModel.specsModel)
        view.productCardView.setProductModel(comparisonModel.productCardModel)
        view.productCardView.setOnClickListener {
            Toast.makeText(context, "Clicked: "+comparisonModel.productCardModel.productName, Toast.LENGTH_SHORT)
                    .show()
        }
    }
}