package com.tokopedia.recommendation_widget_common.widget.comparison.specs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R

class SpecsAdapter(var listModel: SpecsListModel): RecyclerView.Adapter<SpecsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spec, parent, false)
        if (viewType != -1 && viewType < listModel.specs.size) {
            val layoutParams = view.layoutParams
            layoutParams.height = listModel.specsConfig.heightPositionMap[viewType]?:0
            view.layoutParams = layoutParams
        }
        return SpecsItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listModel.specs.size
    }

    //viewType == position
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SpecsItemViewHolder, position: Int) {
        if (position < listModel.specs.size) {
            holder.bind(listModel.specs[position])
        }
    }
}