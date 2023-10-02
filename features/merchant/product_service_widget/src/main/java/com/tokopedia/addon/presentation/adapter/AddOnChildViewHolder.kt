package com.tokopedia.addon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemAddonChildBinding
import com.tokopedia.utils.view.binding.viewBinding

class AddOnChildViewHolder(
    itemView: View,
    onClickListener: (index: Int, isChecked: Boolean) -> Unit,
    onHelpClickListener: (index: Int) -> Unit,
    private val onItemImpression: (index: Int, addOnUIModel: AddOnUIModel) -> Unit
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

    fun bind(item: AddOnUIModel, showDescription: Boolean) {
        binding?.apply {
            tfName.text = item.name
            tfPrice.text = item.discountedPrice.getCurrencyFormatted()
            tfSlashedPrice.text = item.price.getCurrencyFormatted()
            tfSlashedPrice.strikethrough()
            tfSlashedPrice.isVisible = item.isDiscounted()
            cuAddon.isChecked = item.isSelected
            cuAddon.isEnabled = !item.isMandatory
            root.isEnabled = !item.isMandatory
            tfDescription.text = item.description
            tfDescription.isVisible = showDescription
        }
        itemView.addOnImpressionListener(item.impressHolder) {
            onItemImpression(bindingAdapterPosition, item)
        }
    }

}
