package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.databinding.FilterCategoryDetailLevelThreeViewHolderBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class FilterCategoryLevelThreeViewHolder(
        itemView: View,
        private val callback: FilterCategoryLevelThreeAdapterCallback
) : RecyclerView.ViewHolder(itemView) {
    private var binding: FilterCategoryDetailLevelThreeViewHolderBinding? by viewBinding()

    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if(adapterPosition != RecyclerView.NO_POSITION) {
            callback.onLevelThreeCategoryClicked(adapterPosition)
        }
    }

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
        filterRadioButton.setOnCheckedChangeListener(checkedChangeListener)
    }

    interface  FilterCategoryLevelThreeAdapterCallback {
        fun onLevelThreeCategoryClicked(position: Int)
    }
}