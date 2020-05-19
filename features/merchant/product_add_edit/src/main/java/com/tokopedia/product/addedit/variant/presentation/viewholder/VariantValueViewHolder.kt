package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import kotlinx.android.synthetic.main.item_variant_type.view.*

class VariantValueViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    var isSelected = false

    init {
        itemView.chipsVariantTypeName.setOnRemoveListener {

        }
    }

    fun bindData(nameRecommendation: String) {
        itemView.chipsVariantTypeName.setTag(R.id.name, nameRecommendation)
        itemView.chipsVariantTypeName.chip_text.text = nameRecommendation
    }
}