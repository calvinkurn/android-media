package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogTopFiveDataModel
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogTopFiveItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener): AbstractViewHolder<CatalogTopFiveDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_catalog_top_five
    }

    private val topFiveImage = view.findViewById<ImageUnify>(R.id.catalog_top_five_product_image)
    private val topFiveTitle = view.findViewById<Typography>(R.id.catalog_top_five_product_title)
    private val topFiveRank = view.findViewById<Typography>(R.id.catalog_top_five_rank)
    private val topFiveLayout = view.findViewById<CardUnify2>(R.id.catalog_top_five_layout)

    override fun bind(element: CatalogTopFiveDataModel?) {
        element?.catalogTopFiveList?.imageUrl?.let { iconUrl ->
            topFiveImage.loadImageWithoutPlaceholder(iconUrl)
        }
        topFiveTitle.text = element?.catalogTopFiveList?.name
        topFiveRank.text = "#${element?.catalogTopFiveList?.rank}"
        topFiveLayout.setOnClickListener {
            catalogLibraryListener.onProductCardClicked(element?.catalogTopFiveList?.applink)
        }
    }
}
