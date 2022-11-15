package com.tokopedia.catalog_library.viewholder.components

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogTopFiveItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(catalogListResponse: CatalogListResponse.CatalogGetList.CatalogsList, catalogLibraryListener: CatalogLibraryListener){
        catalogListResponse.imageUrl?.let { iconUrl ->
            view.findViewById<ImageUnify>(R.id.catalog_top_five_product_image).loadImageWithoutPlaceholder(iconUrl)
        }
        view.findViewById<Typography>(R.id.catalog_top_five_product_title).text = catalogListResponse.name

        view.findViewById<LinearLayout>(R.id.catalog_top_five_layout).setOnClickListener {
            catalogLibraryListener.onProductCardClicked(catalogListResponse.applink)
        }
    }
}
