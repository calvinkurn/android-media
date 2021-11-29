package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.databinding.FilterCategoryDetailLevelThreeViewHolderBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class FilterCategoryLevelThreeAdapter(
        private val filterCategoryLevelThreeViewModelList: List<FilterCategoryLevelThreeViewModel>,
        private val callback: FilterCategoryDetailCallback
) : RecyclerView.Adapter<FilterCategoryLevelThreeAdapter.FilterCategoryLevelThreeViewHolder>() {

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

    inner class FilterCategoryLevelThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: FilterCategoryDetailLevelThreeViewHolderBinding? by viewBinding()

        init {
            bindContainer()
        }

        fun bind(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
            bindTitle(filterCategoryLevelThreeViewModel)
            bindCheckbox(filterCategoryLevelThreeViewModel)
        }

        private fun bindContainer() {
            binding?.filterCategoryDetailLevelThreeContainer?.setOnClickListener {
                val filterRadioButton = binding?.filterCategoryDetailLevelThreeRadioButton ?: return@setOnClickListener

                filterRadioButton.isChecked = filterRadioButton.isChecked != true
            }
        }

        private fun bindTitle(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
            binding?.filterCategoryDetailLevelThreeTitle?.text = filterCategoryLevelThreeViewModel.levelThreeCategory.name
        }

        private fun bindCheckbox(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
            val filterRadioButton = binding?.filterCategoryDetailLevelThreeRadioButton ?: return

            filterRadioButton.setOnCheckedChangeListener(null)
            filterRadioButton.isChecked = filterCategoryLevelThreeViewModel.isSelected
            filterRadioButton.setOnCheckedChangeListener { _, isChecked ->
                callback.onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModel, isChecked)
            }
        }
    }
}