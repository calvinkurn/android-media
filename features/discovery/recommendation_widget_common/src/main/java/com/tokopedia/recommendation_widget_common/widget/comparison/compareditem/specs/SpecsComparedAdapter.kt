package com.tokopedia.recommendation_widget_common.widget.comparison.compareditem.specs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsListModel

class SpecsComparedAdapter(var listModel: SpecsListModel): RecyclerView.Adapter<SpecsComparedItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecsComparedItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spec_compared_item, parent, false)
        if (viewType != -1 && viewType < listModel.specs.size) {
            val layoutParams = view.layoutParams
            layoutParams.height = listModel.specsConfig.heightPositionMap[viewType]?:0
            view.layoutParams = layoutParams
        }
        return SpecsComparedItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listModel.specs.size
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SpecsComparedItemViewHolder, position: Int) {
        if (position < listModel.specs.size) {
            holder.bind(listModel.specs[position], position, listModel.currentRecommendationPosition, listModel.totalRecommendations)
        }
    }
}