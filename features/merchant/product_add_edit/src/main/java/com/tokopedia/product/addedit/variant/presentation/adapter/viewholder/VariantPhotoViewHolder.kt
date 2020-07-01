package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.unifycomponents.setImage
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

    fun bindData(data: VariantPhoto) {
        itemView.typographyVariantPhoto.text = data.variantUnitValueName
        if (data.imageUrlOrPath.isNotBlank()) {
            itemView.ivAddIndicator.hide()
            if (data.imageUrlOrPath.isNotBlank()) itemView.ivVariantPhoto.setImage(data.imageUrlOrPath, 0F)
        }
    }
}