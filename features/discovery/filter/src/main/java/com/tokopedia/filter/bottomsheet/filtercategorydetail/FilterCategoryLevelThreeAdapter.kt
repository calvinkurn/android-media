package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R

internal class FilterCategoryLevelThreeAdapter(
        private val filterCategoryLevelThreeViewModelList: List<FilterCategoryLevelThreeViewModel>,
        private val callback: FilterCategoryDetailCallback
) : RecyclerView.Adapter<FilterCategoryLevelThreeViewHolder>(), FilterCategoryLevelThreeViewHolder.FilterCategoryLevelThreeAdapterCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryLevelThreeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_category_detail_level_three_view_holder, parent, false)

        return FilterCategoryLevelThreeViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return filterCategoryLevelThreeViewModelList.size
    }

    override fun onBindViewHolder(holder: FilterCategoryLevelThreeViewHolder, position: Int) {
        holder.bind(filterCategoryLevelThreeViewModelList[position])
    }

    override fun onLevelThreeCategoryClicked(position: Int) {
        callback.onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModelList[position])
    }
}
