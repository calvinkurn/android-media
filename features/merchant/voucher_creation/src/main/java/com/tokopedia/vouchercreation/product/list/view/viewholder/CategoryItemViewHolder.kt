package com.tokopedia.vouchercreation.product.list.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.CategorySelection

class CategoryItemViewHolder(
        private val binding: ItemMvcFilterLayoutBinding,
        private val clickListener: OnListItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnListItemClickListener {
        fun onListItemClicked(isChecked: Boolean, position: Int)
    }

    init {
        binding.root.setOnClickListener {
            val isChecked = binding.ivGreenCheck.isVisible
            if (isChecked) {
                binding.ivGreenCheck.hide()
            } else {
                binding.ivGreenCheck.show()
            }
            // val isChecked is no longer relevant at this point
            clickListener.onListItemClicked(binding.ivGreenCheck.isVisible, adapterPosition)
        }
    }

    fun bindData(categorySelection: CategorySelection) {
        // bind category name
        binding.tpgSelectionName.text = categorySelection.name
        // bind selection state
        if (categorySelection.isSelected) binding.ivGreenCheck.show()
        else binding.ivGreenCheck.hide()
    }
}