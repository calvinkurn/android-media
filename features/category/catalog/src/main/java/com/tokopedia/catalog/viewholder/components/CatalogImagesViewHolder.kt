package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_image.view.*

class CatalogImagesViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(model: CatalogImage, catalogDetailListener: CatalogDetailListener?) {
        model.imageURL?.let {
            itemView.image.loadImage(it)
        }
        itemView.setOnClickListener {
            catalogDetailListener?.onProductImageClick(model,adapterPosition)
        }
    }
}