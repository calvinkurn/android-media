package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.datamodel.CatalogForYouModel
import com.tokopedia.catalog.model.datamodel.CatalogStaggeredProductModel
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class CatalogForYouViewHolder(itemView: View, private val catalogDetailListener: CatalogDetailListener?)
    : AbstractViewHolder<CatalogForYouModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.item_catalog_for_you

    }

    override fun bind(model: CatalogForYouModel) {
        itemView.findViewById<Typography>(R.id.catalog_for_you_name).displayTextOrHide(model.item.name ?: "")
        itemView.findViewById<ImageUnify>(R.id.catalog_for_you_image).apply {
            model.item.catalogImage?.firstOrNull()?.imageUrl?.let { imageUrl ->
                loadImageWithoutPlaceholder(imageUrl)
            }
        }
        itemView.setOnClickListener {
            catalogDetailListener?.onCatalogForYouClick(model.item)
        }
    }
}
