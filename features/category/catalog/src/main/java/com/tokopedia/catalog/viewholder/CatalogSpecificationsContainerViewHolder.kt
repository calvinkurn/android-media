package com.tokopedia.catalog.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel

class CatalogSpecificationsContainerViewHolder(private val view : View,
                                               private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogSpecificationDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_catalog_specifications_container
    }

    override fun bind(element: CatalogSpecificationDataModel?) {
        TODO("Not yet implemented")
    }
}