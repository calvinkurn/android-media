package com.tokopedia.addon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemAddonChildBinding

class AddOnChildViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_addon_child, parent, false)
    }

    private val binding: ItemAddonChildBinding? by viewBinding()

    fun bind(item: AddOnUIModel) {
        binding?.apply {
            tfName.text = item.name
            tfPrice.text = item.priceFormatted
            cuAddon.isChecked = item.isSelected
        }
    }

}
