package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.catalogcard.CatalogCardView
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.CatalogItem

abstract class CatalogCardViewHolder(itemView: View) : AbstractViewHolder<CatalogItem>(itemView) {

    protected val context = itemView.context!!

    override fun bind(catalogItem: CatalogItem) {
        initCatalogImageUrl(catalogItem)
        initCatalogCount(catalogItem)
        initCatalogName(catalogItem)
        initCatalogDescription(catalogItem)
        initCatalogPrice(catalogItem)
        setMulaiDariText()
    }

    private fun setMulaiDariText() {
        getCatalogCardView()?.setLabelMualaiDariText("Mulai dari")

    }

    private fun initCatalogImageUrl(catalogItem: CatalogItem) {
        catalogItem.imageUri?.let { getCatalogCardView()?.setImageCatalogUrl(it) }
    }

    private fun initCatalogCount(catalogItem: CatalogItem) {
        getCatalogCardView()?.setLabelCatalogCountText((catalogItem.countProduct).toString() + " Produk")

    }

    private fun initCatalogName(catalogItem: CatalogItem) {
        catalogItem.name?.let { getCatalogCardView()?.setLabelCatalogNameText(it) }

    }

    private fun initCatalogDescription(catalogItem: CatalogItem) {
        catalogItem.description?.let { getCatalogCardView()?.setLabelCatalogDescriptionText(it) }

    }

    private fun initCatalogPrice(catalogItem: CatalogItem) {
        catalogItem.priceMin?.let { getCatalogCardView()?.setLabelCatalogPriceText(it) }

    }

    protected abstract fun getCatalogCardView(): CatalogCardView?

}