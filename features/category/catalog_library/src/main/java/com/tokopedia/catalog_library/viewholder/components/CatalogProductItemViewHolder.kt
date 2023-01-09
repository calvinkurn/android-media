package com.tokopedia.catalog_library.viewholder.components

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogProductDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogProductItemViewHolder(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : AbstractViewHolder<CatalogProductDataModel>(view) {

    private val productImage = view.findViewById<ImageUnify>(R.id.catalog_product_image)
    private val productTitle = view.findViewById<Typography>(R.id.catalog_product_title)
    private val productPrice = view.findViewById<Typography>(R.id.catalog_product_price)
    private val productLayout = view.findViewById<ConstraintLayout>(R.id.product_layout)

    companion object {
        val LAYOUT = R.layout.item_catalog_product
    }

    override fun bind(element: CatalogProductDataModel?) {
        val priceText =
            "${element?.catalogProduct?.marketPrice?.minFmt} - ${element?.catalogProduct?.marketPrice?.maxFmt}"
        productPrice?.let {
            it.text = priceText
        }
        element?.catalogProduct?.imageUrl?.let { iconUrl ->
            productImage.loadImage(iconUrl)
        }
        productTitle?.let {
            it.text = element?.catalogProduct?.name
            it.setOnClickListener {
                catalogLibraryListener.onCategoryItemClicked(element?.catalogProduct?.name)
            }
        }
        productLayout.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(element?.catalogProduct?.name)
        }
    }
}
