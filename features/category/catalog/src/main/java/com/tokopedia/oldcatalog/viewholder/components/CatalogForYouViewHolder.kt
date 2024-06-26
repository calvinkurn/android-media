package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.listener.CatalogProductCardListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogForYouModel
import com.tokopedia.oldcatalog.model.datamodel.CatalogStaggeredProductModel
import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse
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
        bindName(model)
        bindImage(model)
        setClickListener(model)
    }

    private fun bindName(model: CatalogForYouModel) {
        itemView.findViewById<Typography>(R.id.catalog_for_you_name).displayTextOrHide(model.item?.name ?: "")
    }

    private fun bindImage(model: CatalogForYouModel) {
        itemView.findViewById<ImageUnify>(R.id.catalog_for_you_image).apply {
            model.item?.catalogImage?.firstOrNull()?.imageUrl?.let { imageUrl ->
                loadImageWithoutPlaceholder(imageUrl)
            }
        }
    }

    private fun setClickListener(model: CatalogForYouModel) {
        if(model.item != null) {
            itemView.setOnClickListener {
                catalogDetailListener?.onCatalogForYouClick(adapterPosition, model.item)
            }
        }
    }
}
