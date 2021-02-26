package com.tokopedia.catalog.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel

class CatalogProductsContainerViewHolder(private val view : View,
                                         private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogProductsContainerDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_catalog_products_container
    }

    override fun bind(element: CatalogProductsContainerDataModel) {

    }
}