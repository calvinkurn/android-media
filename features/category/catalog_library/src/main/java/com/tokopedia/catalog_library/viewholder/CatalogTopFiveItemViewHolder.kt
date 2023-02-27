package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogTopFiveDataModel
import com.tokopedia.catalog_library.util.AnalyticsCategoryLandingPage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogTopFiveItemViewHolder(
    private val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : AbstractViewHolder<CatalogTopFiveDataModel>(view) {
    private var dataModel: CatalogTopFiveDataModel? = null

    private val topFiveImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_top_five_product_image)
    }

    private val topFiveTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_top_five_product_title)
    }

    private val topFiveRank: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_top_five_rank)
    }

    private val topFiveLayout: CardUnify2 by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_top_five_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_top_five
    }

    override fun bind(element: CatalogTopFiveDataModel?) {
        dataModel = element

        val catalogTopFiveList = element?.catalogTopFiveList

        catalogTopFiveList?.imageUrl?.let { iconUrl ->
            topFiveImage.loadImage(iconUrl)
        }
        topFiveTitle.text = catalogTopFiveList?.name ?: ""
        topFiveRank.text = String.format(
            view.context.getString(R.string.top_five_rank),
            catalogTopFiveList?.rank ?: ""
        )
        topFiveLayout.setOnClickListener {
            AnalyticsCategoryLandingPage.sendClickCatalogOnTopCatalogsInCategoryEvent(
                dataModel?.categoryName ?: "",
                catalogTopFiveList?.categoryID ?: "",
                catalogTopFiveList?.name ?: "",
                catalogTopFiveList?.id ?: "",
                catalogTopFiveList?.rank ?: -1,
                UserSession(itemView.context).userId
            )
            catalogLibraryListener.onProductCardClicked(catalogTopFiveList?.applink)
        }
    }
}
