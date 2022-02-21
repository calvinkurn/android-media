package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogForYouViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: CatalogComparisonProductsResponse.CatalogComparisonList.CatalogComparison?,
             catalogProductCardListener: CatalogProductCardListener?) {

        itemView.findViewById<Typography>(R.id.catalog_for_you_name).displayTextOrHide(model?.name ?: "")
        itemView.findViewById<ImageUnify>(R.id.catalog_for_you_image).apply {
            model?.catalogImage?.firstOrNull()?.imageUrl?.let { imageUrl ->
                loadImageWithoutPlaceholder(imageUrl)
            }
            setOnClickListener {
                catalogProductCardListener?.onCatalogProductClicked(model)
            }
        }
    }
}