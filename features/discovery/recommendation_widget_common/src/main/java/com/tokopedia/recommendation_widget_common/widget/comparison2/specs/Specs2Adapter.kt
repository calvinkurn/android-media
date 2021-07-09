package com.tokopedia.recommendation_widget_common.widget.comparison2.specs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.specs.SpecsListModel

class Specs2Adapter(var listModel: SpecsListModel): RecyclerView.Adapter<Specs2ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Specs2ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spec2, parent, false)
        if (viewType != -1 && viewType < listModel.specs.size) {
            val layoutParams = view.layoutParams
            layoutParams.height = listModel.specsConfig.heightPositionMap[viewType]?:0
            view.layoutParams = layoutParams
        }
        return Specs2ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listModel.specs.size
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: Specs2ItemViewHolder, position: Int) {
        if (position < listModel.specs.size) {
            holder.bind(listModel.specs[position], position, listModel.currentRecommendationPosition, listModel.totalRecommendations)
        }
    }
}