package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.ChipsUnify

class VariantValueViewHolder(
    itemView: View,
    removeButtonClickListener: OnRemoveButtonClickListener
) : RecyclerView.ViewHolder(itemView) {

    private var chipsVariantValueName: ChipsUnify? = itemView.findViewById(R.id.chipsVariantValueName)

    interface OnRemoveButtonClickListener {
        fun onRemoveButtonClicked(position: Int)
    }

    init {
        chipsVariantValueName?.setOnRemoveListener {
            removeButtonClickListener.onRemoveButtonClicked(bindingAdapterPosition)
        }
    }

    fun bindData(text: String) {
        chipsVariantValueName?.chip_text?.text = text
    }
}
