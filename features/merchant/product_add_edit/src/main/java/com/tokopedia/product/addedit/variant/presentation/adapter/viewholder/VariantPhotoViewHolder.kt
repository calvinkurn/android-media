package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
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
        val zeroDimen = getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl0)
        val sixTeenDimen = getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl4)
        if (adapterPosition == 0) {
            itemView.setMargin(sixTeenDimen, zeroDimen, zeroDimen, zeroDimen)
        }
        itemView.typographyVariantPhoto.text = data.variantUnitValueName
        if (data.imageUrlOrPath.isNotBlank()) {
            itemView.ivAddIndicator.hide()
            itemView.ivVariantPhoto.setImage(data.imageUrlOrPath, zeroDimen.toFloat())
        } else {
            itemView.ivAddIndicator.show()
            itemView.ivVariantPhoto.setImageDrawable(null) // clear image
        }
    }

    private fun getDimension(dimenId: Int): Int {
        return itemView.context.resources.getDimension(dimenId).toInt()
    }
}