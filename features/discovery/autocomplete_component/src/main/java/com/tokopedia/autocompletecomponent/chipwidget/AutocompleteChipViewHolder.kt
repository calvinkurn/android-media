package com.tokopedia.autocompletecomponent.chipwidget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteChipBinding
import com.tokopedia.utils.view.binding.viewBinding

class AutocompleteChipViewHolder(
        itemView: View,
        private val clickListener: AutocompleteChipWidgetViewListener
) : RecyclerView.ViewHolder(itemView) {
    private var binding: LayoutAutocompleteChipBinding? by viewBinding()

    fun bind(item: AutocompleteChipDataView) {
        val binding = binding ?: return
        bindText(item, binding)
        bindListener(item, binding)
    }

    private fun bindText(item: AutocompleteChipDataView, binding: LayoutAutocompleteChipBinding) {
        binding.autocompleteChip.chipText = item.title
    }

    private fun bindListener(item: AutocompleteChipDataView, binding: LayoutAutocompleteChipBinding) {
        binding.autocompleteChip.setOnClickListener {
            clickListener.onChipClicked(item, adapterPosition)
        }
    }
}