package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogPopularBrandsDM
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

class CatalogPopularBrandsItemVH(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : AbstractViewHolder<CatalogPopularBrandsDM>(view) {

    private var dataModel: CatalogPopularBrandsDM? = null

    private val brandImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_brand_image)
    }

    companion object {
        val LAYOUT = R.layout.item_home_page_popular_brands
    }

    override fun bind(element: CatalogPopularBrandsDM?) {
        dataModel = element
        val brand = dataModel?.brandsList
        brand?.imageUrl?.let { iconUrl ->
            brandImage.loadImage(iconUrl)
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.brandsList?.let {
        }
    }
}
