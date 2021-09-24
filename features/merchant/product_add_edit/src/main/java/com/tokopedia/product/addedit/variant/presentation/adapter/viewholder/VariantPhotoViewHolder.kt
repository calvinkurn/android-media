package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifyprinciples.Typography

class VariantPhotoViewHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

    private var typographyVariantPhoto: Typography? = null
    private var ivVariantPhoto: ImageView? = null

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    init {
        itemView.setOnClickListener {
            onItemClickListener.onItemClicked(adapterPosition)
        }
    }

    fun bindData(data: VariantPhoto) {
        typographyVariantPhoto?.text = data.variantUnitValueName
        if (data.imageUrlOrPath.isNotBlank()) {
            ivVariantPhoto?.setImage(data.imageUrlOrPath, 0F)
        } else {
            ivVariantPhoto?.scaleType = ImageView.ScaleType.CENTER_INSIDE
            ivVariantPhoto?.setImageResource(com.tokopedia.product.addedit.R.drawable.ic_plus_gray)
        }
    }
}