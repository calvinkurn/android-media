package com.tokopedia.catalog_library.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogSpecialItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    companion object {
        val LAYOUT = R.layout.catalog_special_container
    }

    fun bind(
        catalogSpecialData: CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialData,
        catalogLibraryListener: CatalogLibraryListener
    ) {
        catalogSpecialData.iconUrl?.let { iconUrl ->
            val imageView = view.findViewById<ImageUnify>(R.id.special_icon) as ImageUnify
            imageView.loadImage(iconUrl)
        }
        view.findViewById<Typography>(R.id.special_title)?.text =
            catalogSpecialData.name
        view.findViewById<View>(R.id.catalog_special_item_layout).setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(catalogSpecialData.name)
        }
    }
}
