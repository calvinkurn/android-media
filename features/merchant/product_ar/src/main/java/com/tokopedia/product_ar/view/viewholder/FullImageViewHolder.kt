package com.tokopedia.product_ar.view.viewholder

import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_ar.R
import com.tokopedia.unifycomponents.ImageUnify

class FullImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.full_image_view_holder
    }

    private val imgFull: ImageUnify? = itemView.findViewById(R.id.img_full)

    fun bind(image: Bitmap) {
        imgFull?.loadImage(image)
    }
}