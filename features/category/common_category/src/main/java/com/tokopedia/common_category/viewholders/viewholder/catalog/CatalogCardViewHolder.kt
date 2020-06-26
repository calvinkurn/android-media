package com.tokopedia.common_category.viewholders.viewholder.catalog

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.catalogcard.CatalogCardView
import com.tokopedia.common_category.data.catalogModel.CatalogItem
import com.tokopedia.common_category.interfaces.CatalogCardListener

abstract class CatalogCardViewHolder(itemView: View, var catalogCardListener: CatalogCardListener) : AbstractViewHolder<CatalogItem>(itemView) {

    protected val context = itemView.context!!

    override fun bind(catalogItem: CatalogItem) {
        initCatalogImageUrl(catalogItem)
        initCatalogCount(catalogItem)
        initCatalogName(catalogItem)
        initCatalogDescription(catalogItem)
        initCatalogPrice(catalogItem)
        setMulaiDariText()
        initCatalogCardClick(catalogItem)
    }

    private fun initCatalogCardClick(catalogItem: CatalogItem) {
        getCatalogCardView()?.setOnClickListener {
            catalogCardListener.setOnCatalogClicked(catalogItem.id.toString(), catalogItem.name.toString())
        }
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