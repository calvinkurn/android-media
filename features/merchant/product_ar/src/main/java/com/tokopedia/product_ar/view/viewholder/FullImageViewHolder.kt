package com.tokopedia.product_ar.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ComparissonImageUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class FullImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.full_image_view_holder
    }

    private val imgFull: ImageUnify? = itemView.findViewById(R.id.img_full)
    private val txtTitle: Typography? = itemView.findViewById(R.id.txt_title_var)

    fun bind(data: ComparissonImageUiModel) {
        imgFull?.loadImage(data.bitmap)
        txtTitle?.text = data.productName
    }
}