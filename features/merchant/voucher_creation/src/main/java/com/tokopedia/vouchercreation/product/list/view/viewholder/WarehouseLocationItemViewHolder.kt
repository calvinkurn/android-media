package com.tokopedia.vouchercreation.product.list.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.model.WarehouseLocationSelection

class WarehouseLocationItemViewHolder(
        private val binding: ItemMvcFilterLayoutBinding,
        private val clickListener: OnListItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnListItemClickListener {
        fun onListItemClicked(position: Int)
    }

    init {
        binding.root.setOnClickListener {
            val isChecked = binding.ivGreenCheck.isVisible
            if (isChecked) {
                binding.ivGreenCheck.hide()
            } else {
                binding.ivGreenCheck.show()
                clickListener.onListItemClicked(adapterPosition)
            }
        }
    }

    fun bindData(warehouseLocationSelection: WarehouseLocationSelection) {
        // bind warehouse name
        binding.tpgSelectionName.text = warehouseLocationSelection.warehouseName
        // bind selection state
        if (warehouseLocationSelection.isSelected) binding.ivGreenCheck.show()
        else binding.ivGreenCheck.hide()
    }
}