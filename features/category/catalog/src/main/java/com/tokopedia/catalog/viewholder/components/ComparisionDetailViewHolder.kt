package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify

class ComparisionDetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(baseCatalog: ComparisionModel?, comparisionCatalog: ComparisionModel?, catalogDetailListener: CatalogDetailListener) {
        baseCatalog?.run {
            itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.first_catalog_product_brand).displayTextOrHide(brand ?: "")
            itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.first_catalog_product_name).displayTextOrHide(name ?: "")
            itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.first_catalog_product_price).displayTextOrHide(price ?: "")
            url?.let {imageUrl ->
                itemView.findViewById<ImageUnify>(R.id.first_catalog_image).loadImageWithoutPlaceholder(imageUrl)
            }
        }
        comparisionCatalog?.run {
            itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.second_catalog_product_brand).displayTextOrHide(brand ?: "")
            itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.second_catalog_product_name).displayTextOrHide(name ?: "")
            itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.second_catalog_product_price).displayTextOrHide(price ?: "")
            url?.let {imageUrl ->
                itemView.findViewById<ImageUnify>(R.id.second_catalog_image).loadImageWithoutPlaceholder(imageUrl)
            }
        }

        itemView.findViewById<CardUnify>(R.id.comparision_card).setOnClickListener {
            comparisionCatalog?.id?.let {catalogId ->
                if(catalogId.isNotEmpty()){
                    catalogDetailListener.comparisionCatalogClicked(catalogId)
                }
            }
        }
    }
}
