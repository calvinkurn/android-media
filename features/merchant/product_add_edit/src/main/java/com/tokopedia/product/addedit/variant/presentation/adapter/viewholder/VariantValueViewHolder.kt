package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_variant_value.view.*

class VariantValueViewHolder(itemView: View, removeButtonClickListener: OnRemoveButtonClickListener) : RecyclerView.ViewHolder(itemView) {

    interface OnRemoveButtonClickListener {
        fun onRemoveButtonClicked(position: Int)
    }

    init {
        itemView.chipsVariantValueName.setOnRemoveListener {
            removeButtonClickListener.onRemoveButtonClicked(adapterPosition)
        }
    }

    fun bindData(text: String) {
        itemView.chipsVariantValueName.chip_text.text = text
    }
}