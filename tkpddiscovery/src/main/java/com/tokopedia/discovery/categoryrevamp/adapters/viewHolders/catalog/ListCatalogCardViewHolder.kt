package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.catalogcard.CatalogCardView
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CatalogCardListener
import kotlinx.android.synthetic.main.catalog_card_list.view.*


class ListCatalogCardViewHolder(itemView: View,catalogCardListener: CatalogCardListener) : CatalogCardViewHolder(itemView,catalogCardListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.catalog_card_list
    }


    override fun getCatalogCardView(): CatalogCardView? {
        return itemView.catalogCardView ?: null
    }

}
