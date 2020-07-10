package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
            itemView.ivVariantPhoto.setImage(data.imageUrlOrPath, 0F)
        } else {
            itemView.ivAddIndicator.show()
            itemView.ivVariantPhoto.setImageDrawable(null) // clear image
        }
    }
}