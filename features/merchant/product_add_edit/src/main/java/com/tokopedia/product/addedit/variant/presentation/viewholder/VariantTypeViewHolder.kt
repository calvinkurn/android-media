package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_variant_type.view.*

class VariantTypeViewHolder(itemView: View, clickListener: OnVariantTypeClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnVariantTypeClickListener {
        fun onVariantTypeClicked(position: Int)
    }

    var isSelected = false

    init {
        itemView.chipsVariantTypeName.setOnClickListener {
            if (isSelected) {
                isSelected = false
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_NORMAL
            } else {
                isSelected = true
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_SELECTED
            }
            clickListener.onVariantTypeClicked(adapterPosition)
        }
    }

    fun bindData(text: String, isSelected: Boolean) {
        itemView.chipsVariantTypeName.chip_text.text = text
    }
}