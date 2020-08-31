package com.tokopedia.common_category.viewholders.viewholder.catalog

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.common_category.R
import com.tokopedia.common_category.catalogcard.CatalogCardView
import com.tokopedia.common_category.interfaces.CatalogCardListener
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
