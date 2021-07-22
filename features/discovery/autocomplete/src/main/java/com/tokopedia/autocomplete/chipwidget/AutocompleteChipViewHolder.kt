package com.tokopedia.autocomplete.chipwidget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_autocomplete_chip.view.*

class AutocompleteChipViewHolder(
        itemView: View,
        private val clickListener: AutocompleteChipWidgetViewListener
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: AutocompleteChipDataView) {
        bindText(item)
        bindListener(item)
    }

    private fun bindText(item: AutocompleteChipDataView) {
        itemView.autocompleteChip?.chipText = item.title
    }

    private fun bindListener(item: AutocompleteChipDataView) {
        itemView.autocompleteChip?.setOnClickListener {
            clickListener.onChipClicked(item)
        }
    }
}