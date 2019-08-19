package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.catalogcard.CatalogCardView
import kotlinx.android.synthetic.main.catalog_card_big_grid.view.*

class BigGridCatalogCardViewHolder(itemView: View) : CatalogCardViewHolder(itemView) {


    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.catalog_card_big_grid
    }

    override fun getCatalogCardView(): CatalogCardView? {
        return itemView.catalogCardView ?: null
    }
}