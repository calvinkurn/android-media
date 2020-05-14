package com.tokopedia.common_category.viewholders.catalog

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.common_category.R
import com.tokopedia.common_category.catalogcard.CatalogCardView
import com.tokopedia.common_category.interfaces.CatalogCardListener
import kotlinx.android.synthetic.main.catalog_card_big_grid.view.*

class BigGridCatalogCardViewHolder(itemView: View,catalogCardListener: CatalogCardListener) : CatalogCardViewHolder(itemView,catalogCardListener) {


    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.catalog_card_big_grid
    }

    override fun getCatalogCardView(): CatalogCardView? {
        return itemView.catalogCardView ?: null
    }
}