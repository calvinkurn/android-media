package com.tokopedia.catalog_library.viewholder.components

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogRelevantItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(catalogRelevantResponse: CatalogRelevantResponse.Catalogs, catalogLibraryListener: CatalogLibraryListener){
        catalogRelevantResponse.imageUrl?.let { iconUrl ->
            view.findViewById<ImageUnify>(R.id.catalog_relevant_image).loadImageWithoutPlaceholder(iconUrl)
        }
        view.findViewById<Typography>(R.id.catalog_relevant_product_title).text = catalogRelevantResponse.name
        view.findViewById<ConstraintLayout>(R.id.catalog_relevant_layout).setOnClickListener {
            catalogLibraryListener.onProductCardClicked(catalogRelevantResponse.applink)
        }
    }
}
