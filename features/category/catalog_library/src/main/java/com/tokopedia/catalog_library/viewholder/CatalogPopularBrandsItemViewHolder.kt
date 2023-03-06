package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogPopularBrandsDataModel
import com.tokopedia.catalog_library.util.AnalyticsHomePage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogPopularBrandsItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : AbstractViewHolder<CatalogPopularBrandsDataModel>(view) {

    private var dataModel: CatalogPopularBrandsDataModel? = null

    private val relevantImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_brand_image)
    }

    companion object {
        val LAYOUT = R.layout.item_home_page_popular_brands
    }

    override fun bind(element: CatalogPopularBrandsDataModel?) {
        dataModel = element
        val brand = dataModel?.brandsList
        brand?.imageUrl?.let { iconUrl ->
            relevantImage.loadImage(iconUrl)
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.brandsList?.let {
        }
    }
}
