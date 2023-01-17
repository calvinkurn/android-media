package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogTopFiveDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogTopFiveItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener): AbstractViewHolder<CatalogTopFiveDataModel>(view) {


    private val topFiveImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_top_five_product_image)
    }

    private val topFiveTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_top_five_product_title)
    }

    private val topFiveRank: Typography by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_top_five_rank)
    }

    private val topFiveLayout: CardUnify2 by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_top_five_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_top_five
    }

    override fun bind(element: CatalogTopFiveDataModel?) {
        val catalogTopFiveList = element?.catalogTopFiveList

        catalogTopFiveList?.imageUrl?.let { iconUrl ->
            topFiveImage.loadImage(iconUrl)
        }
        topFiveTitle.text = catalogTopFiveList?.name ?: ""
        topFiveRank.text = "#${catalogTopFiveList?.rank}"
        topFiveLayout.setOnClickListener {
            catalogLibraryListener.onProductCardClicked(catalogTopFiveList?.applink)
        }
    }
}
