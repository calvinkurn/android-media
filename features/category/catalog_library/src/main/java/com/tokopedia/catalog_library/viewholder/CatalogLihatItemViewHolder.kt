package com.tokopedia.catalog_library.viewholder

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDataModel
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogLihatItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener): AbstractViewHolder<CatalogLihatItemDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_lihat_grid
    }

    override fun bind(element: CatalogLihatItemDataModel?) {
        element?.catalogLibraryChildDataListItem?.categoryIconUrl?.let { iconUrl ->
            view.findViewById<ImageUnify>(R.id.lihat_item_icon)
                ?.loadImageWithoutPlaceholder(iconUrl)

        }
        view.findViewById<Typography>(R.id.lihat_item_title)?.text =
            element?.catalogLibraryChildDataListItem?.categoryName
        view.findViewById<LinearLayout>(R.id.lihat_expanded_item_layout).setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(element?.catalogLibraryChildDataListItem?.categoryName)
        }
    }
}
