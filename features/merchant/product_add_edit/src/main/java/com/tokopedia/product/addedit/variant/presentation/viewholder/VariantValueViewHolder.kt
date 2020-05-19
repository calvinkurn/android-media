package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_variant_value.view.*

class VariantValueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.chipsVariantValueName.setOnRemoveListener {

        }
    }

    fun bindData(text: String) {
        itemView.chipsVariantValueName.chip_text.text = text
    }
}