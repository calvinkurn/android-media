package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogRelevantDM
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.util.ActionKeys
import com.tokopedia.catalog_library.util.CatalogAnalyticsHomePage
import com.tokopedia.catalog_library.util.EventKeys
import com.tokopedia.catalog_library.util.TrackerId
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogRelevantItemVH(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : CatalogLibraryAbstractViewHolder<CatalogRelevantDM>(view) {

    private var dataModel: CatalogRelevantDM? = null
    private var relevantProduct : CatalogRelevantResponse.Catalogs? = null

    private val relevantImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_relevant_image)
    }

    private val relevantTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_relevant_product_title)
    }

    private val relevantLayout: ConstraintLayout by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_relevant_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_relevant
    }

    override fun bind(element: CatalogRelevantDM?) {
        dataModel = element
        relevantProduct  = dataModel?.relevantDataList
        renderImage()
        renderTitle()
        setUpClick()
    }

    private fun setUpClick() {
        relevantLayout.setOnClickListener {
            CatalogAnalyticsHomePage.sendClickCatalogOnRelevantCatalogsEvent(
                relevantProduct?.name ?: "",
                layoutPosition + 1,
                relevantProduct?.id ?: "",
                UserSession(itemView.context).userId
            )
            catalogLibraryListener.onProductCardClicked(relevantProduct?.applink)
        }
    }

    private fun renderTitle() {
        relevantTitle.text = relevantProduct?.name ?: ""
    }

    private fun renderImage() {
        relevantProduct?.imageUrl?.let { iconUrl ->
            relevantImage.loadImage(iconUrl)
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.relevantDataList?.let {
            catalogLibraryListener.categoryHorizontalCarouselImpression(
                EventKeys.CREATIVE_NAME_RELEVANT_VALUE,
                layoutPosition + 1,
                dataModel?.relevantDataList?.id.toString(),
                dataModel?.relevantDataList?.name ?: "",
                UserSession(itemView.context).userId,
                TrackerId.IMPRESSION_ON_RELEVANT_CATALOGS,
                ActionKeys.IMPRESSION_ON_RELEVANT_CATALOGS
            )
        }
    }
}
