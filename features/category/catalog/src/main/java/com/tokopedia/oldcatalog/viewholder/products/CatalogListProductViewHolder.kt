package com.tokopedia.oldcatalog.viewholder.products

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogProductCardListener
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem
import com.tokopedia.oldcatalog.model.util.CatalogProductCard
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardListView


open class CatalogListProductViewHolder(itemView: View, private val catalogProductCardListener: CatalogProductCardListener) : AbstractViewHolder<CatalogProductItem>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.item_catalog_preffered_product
    }

    override fun bind(catalogProductItem: CatalogProductItem?) {
        if (catalogProductItem == null) return

        itemView.findViewById<ProductCardListView>(R.id.product_card).apply {
            setProductModel(
                    CatalogProductCard.toCatalogProductModel(catalogProductItem))
//            setThreeDotsOnClickListener {
//                catalogProductCardListener.onThreeDotsClicked(catalogProductItem, adapterPosition)
//            }
            setAddToCartOnClickListener {

            }
            setAddToCartNonVariantClickListener(object : ATCNonVariantListener{
                override fun onQuantityChanged(quantity: Int) {
                }

            })

            setOnClickListener {
                catalogProductCardListener.onItemClicked(catalogProductItem, adapterPosition)
            }
        }
    }
}
