package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogRelevantDataModel
import com.tokopedia.catalog_library.util.AnalyticsHomePage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogRelevantItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) : AbstractViewHolder<CatalogRelevantDataModel>(view) {

    private var dataModel: CatalogRelevantDataModel? = null

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

    override fun bind(element: CatalogRelevantDataModel?) {
        dataModel = element
        val relevantProduct = dataModel?.relevantDataList
        relevantProduct?.imageUrl?.let { iconUrl ->
            relevantImage.loadImage(iconUrl)
        }
        relevantTitle.text = relevantProduct?.name ?: ""
        relevantLayout.setOnClickListener {
            AnalyticsHomePage.sendClickCatalogOnRelevantCatalogsEvent(
                relevantProduct?.name ?: "",
                layoutPosition + 1,
                relevantProduct?.id ?: "",
                UserSession(itemView.context).userId
            )
            catalogLibraryListener.onProductCardClicked(relevantProduct?.applink)
        }
    }

    override fun onViewAttachedToWindow() {
        if (dataModel?.impressHolder?.isInvoke != true) {
            dataModel?.relevantDataList?.let {
                AnalyticsHomePage.sendImpressionOnRelevantCatalogsEvent(
                    layoutPosition + 1,
                    dataModel?.relevantDataList?.id.toString(),
                    dataModel?.relevantDataList?.name ?: "",
                    UserSession(itemView.context).userId
                )
                dataModel?.impressHolder?.invoke()
            }
        }
    }
}
