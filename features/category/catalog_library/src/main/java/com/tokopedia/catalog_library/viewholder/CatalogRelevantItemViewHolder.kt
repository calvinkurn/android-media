package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogRelevantDataModel
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogRelevantItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener): AbstractViewHolder<CatalogRelevantDataModel>(view) {

    private val relevantImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_relevant_image)
    }

    private val relevantTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_relevant_product_title)
    }

    private val relevantLayout: ConstraintLayout by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.catalog_relevant_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_relevant
    }

    override fun bind(element: CatalogRelevantDataModel?) {
        element?.relevantDataList?.imageUrl?.let { iconUrl ->
            relevantImage.loadImage(iconUrl)
        }
        relevantTitle.text = element?.relevantDataList?.name ?: ""
        relevantLayout.setOnClickListener {
            catalogLibraryListener.onProductCardClicked(element?.relevantDataList?.applink)
        }
    }
}
