package com.tokopedia.catalog.viewholder.products

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.util.CatalogProductCard
import com.tokopedia.productcard.ProductCardListView


open class CatalogListProductViewHolder(itemView: View, private val catalogProductCardListener: CatalogProductCardListener) : AbstractViewHolder<CatalogProductItem>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_catalog_preffered_product
    }

    override fun bind(catalogProductItem: CatalogProductItem?) {
        if (catalogProductItem == null) return

        itemView.setOnClickListener {
            catalogProductCardListener.onItemClicked(catalogProductItem, adapterPosition)
        }

        itemView.findViewById<ProductCardListView>(R.id.product_card).apply {
            setProductModel(
                    CatalogProductCard.toCatalogProductModel(catalogProductItem))
            setThreeDotsOnClickListener {
                catalogProductCardListener.onThreeDotsClicked(catalogProductItem, adapterPosition)
            }
        }
    }
}