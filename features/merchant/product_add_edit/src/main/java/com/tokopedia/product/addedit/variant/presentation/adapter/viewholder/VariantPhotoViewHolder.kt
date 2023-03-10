package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifyprinciples.Typography

class VariantPhotoViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {

    private var typographyVariantPhoto: Typography? = itemView.findViewById(R.id.typographyVariantPhoto)
    private var ivVariantPhoto: ImageView? = itemView.findViewById(R.id.ivVariantPhoto)

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    init {
        itemView.setOnClickListener {
            onItemClickListener.onItemClicked(absoluteAdapterPosition)
        }
    }

    fun bindData(data: VariantPhoto) {
        typographyVariantPhoto?.text = data.variantUnitValueName
        if (data.imageUrlOrPath.isNotBlank()) {
            ivVariantPhoto?.loadImage(data.imageUrlOrPath)
        } else {
            ivVariantPhoto?.apply {
                val iconRes = getIconUnifyDrawable(context,
                    IconUnify.ADD, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
                ivVariantPhoto?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                ivVariantPhoto?.loadImage(iconRes)
            }
        }
    }
}
