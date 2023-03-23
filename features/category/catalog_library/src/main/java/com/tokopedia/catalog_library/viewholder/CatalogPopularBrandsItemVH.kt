package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogPopularBrandsDM
import com.tokopedia.catalog_library.util.ActionKeys
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.EventKeys
import com.tokopedia.catalog_library.util.TrackerId
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.user.session.UserSession

class CatalogPopularBrandsItemVH(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : AbstractViewHolder<CatalogPopularBrandsDM>(view) {

    private var dataModel: CatalogPopularBrandsDM? = null

    private val brandImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_brand_image)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_home_page_popular_brands
    }

    override fun bind(element: CatalogPopularBrandsDM?) {
        dataModel = element
        val brand = dataModel?.brandsList
        brand?.imageUrl?.let { iconUrl ->
            brandImage.loadImage(iconUrl)
        }
        brandImage.setOnClickListener {
            catalogLibraryListener.onPopularBrandsHomeClick(dataModel?.brandsList?.name ?: "",
                dataModel?.brandsList?.id ?: "",
                (bindingAdapterPosition + 1).toString()
            )
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.brandsList?.let {
            catalogLibraryListener.categoryHorizontalCarouselImpression(
                EventKeys.CREATIVE_NAME_BRAND_VALUE,
                layoutPosition + 1,
                dataModel?.brandsList?.id.toString(),
                dataModel?.brandsList?.name ?: "",
                UserSession(itemView.context).userId,
                TrackerId.IMPRESSION_ON_POPULAR_BRANDS,
                ActionKeys.IMPRESSION_ON_POPULAR_BRANDS
            )
        }
    }
}
