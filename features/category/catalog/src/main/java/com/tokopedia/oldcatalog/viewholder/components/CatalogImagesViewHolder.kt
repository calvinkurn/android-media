package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_image.view.*

class CatalogImagesViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(model: CatalogImage, catalogDetailListener: CatalogDetailListener?) {
        catalogDetailListener?.sendWidgetImpressionEvent(CatalogDetailAnalytics.ActionKeys.IMAGE_WIDGET_IMPRESSION,
            CatalogDetailAnalytics.ActionKeys.IMAGE_WIDGET_IMPRESSION_ITEM_NAME,
            adapterPosition)
        model.imageURL?.let {
            itemView.image.loadImage(it)
        }
        itemView.setOnClickListener {
            catalogDetailListener?.onProductImageClick(model,adapterPosition)
        }
    }
}
