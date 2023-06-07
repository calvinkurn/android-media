package com.tokopedia.addon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemAddonChildBinding
import com.tokopedia.utils.view.binding.viewBinding

class AddOnChildViewHolder(
    itemView: View,
    onClickListener: (index: Int, isChecked: Boolean) -> Unit,
    onHelpClickListener: (index: Int) -> Unit
): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_addon_child, parent, false)
    }

    private val binding: ItemAddonChildBinding? by viewBinding()

    init {
        binding?.apply {
            root.setOnClickListener {
                cuAddon.performClick()
            }
            cuAddon.setOnClickListener {
                onClickListener(bindingAdapterPosition, cuAddon.isChecked)
            }
            icEdu.setOnClickListener {
                onHelpClickListener(bindingAdapterPosition)
            }
        }
    }

    fun bind(item: AddOnUIModel) {
        binding?.apply {
            tfName.text = item.name
            tfPrice.text = item.priceFormatted
            cuAddon.isChecked = item.isSelected
        }
    }

}
