package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_variant_photo.view.*

class VariantPhotoViewHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    init {
        itemView.setOnClickListener {
            onItemClickListener.onItemClicked(adapterPosition)
        }
    }

    fun bindData(text: String) {
        itemView.typographyVariantPhoto.text = text
    }
}