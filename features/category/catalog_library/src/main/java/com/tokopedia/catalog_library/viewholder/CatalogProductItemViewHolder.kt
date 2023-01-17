package com.tokopedia.catalog_library.viewholder

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

    private val productImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_image)
    }

    private val productTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_title)
    }

    private val productPrice: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_price)
    }

    private val productLayout: ConstraintLayout by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.product_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_product
    }

    override fun bind(element: CatalogProductDataModel?) {
        val catalogProduct = element?.catalogProduct
        productPrice.text = String.format(
            itemView.context.getString(R.string.product_price),
            catalogProduct?.marketPrice?.minFmt, catalogProduct?.marketPrice?.maxFmt
        )

        catalogProduct?.imageUrl?.let { iconUrl ->
            productImage.loadImage(iconUrl)
        }
        productTitle.text = element?.catalogProduct?.name ?: ""
        productLayout.setOnClickListener {
            catalogLibraryListener.onProductCardClicked(catalogProduct?.applink)
        }
    }
}
