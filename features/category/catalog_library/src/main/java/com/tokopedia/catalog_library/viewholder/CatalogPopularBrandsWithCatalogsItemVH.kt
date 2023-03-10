package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogPopularBrandsListDM
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

class CatalogPopularBrandsWithCatalogsItemVH(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : AbstractViewHolder<CatalogPopularBrandsListDM>(view) {

    private var dataModel: CatalogPopularBrandsListDM? = null

    private val brandImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_brand_image)
    }

    companion object {
        val LAYOUT = R.layout.item_popular_brands_with_catalogs
    }

    override fun bind(element: CatalogPopularBrandsListDM?) {
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
