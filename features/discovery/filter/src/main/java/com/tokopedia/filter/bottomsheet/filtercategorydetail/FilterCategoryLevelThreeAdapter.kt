package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import kotlinx.android.synthetic.main.filter_category_detail_level_three_view_holder.view.*

internal class FilterCategoryLevelThreeAdapter(
        private val filterCategoryLevelThreeViewModelList: List<FilterCategoryLevelThreeViewModel>,
        private val callback: FilterCategoryDetailCallback
): RecyclerView.Adapter<FilterCategoryLevelThreeAdapter.FilterCategoryLevelThreeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryLevelThreeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_category_detail_level_three_view_holder, parent, false)

        return FilterCategoryLevelThreeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterCategoryLevelThreeViewModelList.size
    }

    override fun onBindViewHolder(holder: FilterCategoryLevelThreeViewHolder, position: Int) {
        holder.bind(filterCategoryLevelThreeViewModelList[position])
    }

    inner class FilterCategoryLevelThreeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
            bindContainer()
            bindTitle(filterCategoryLevelThreeViewModel)
            bindCheckbox(filterCategoryLevelThreeViewModel)
        }

        private fun bindContainer() {
            itemView.filterCategoryDetailLevelThreeContainer?.setOnClickListener {
                itemView.filterCategoryDetailLevelThreeRadioButton?.isChecked = itemView.filterCategoryDetailLevelThreeRadioButton?.isChecked != true
            }
        }

        private fun bindTitle(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
            itemView.filterCategoryDetailLevelThreeTitle?.text = filterCategoryLevelThreeViewModel.levelThreeCategory.name
        }

        private fun bindCheckbox(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
            itemView.filterCategoryDetailLevelThreeRadioButton?.setOnCheckedChangeListener(null)
            itemView.filterCategoryDetailLevelThreeRadioButton?.isChecked = filterCategoryLevelThreeViewModel.isSelected
            itemView.filterCategoryDetailLevelThreeRadioButton?.setOnCheckedChangeListener { _, isChecked ->
                callback.onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModel, isChecked)
            }
        }
    }
}