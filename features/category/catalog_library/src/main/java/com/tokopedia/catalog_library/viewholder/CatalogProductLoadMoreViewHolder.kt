package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.model.datamodel.CatalogProductLoadMoreDataModel

class CatalogProductLoadMoreViewHolder(val view: View) :
    AbstractViewHolder<CatalogProductLoadMoreDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_product_load_more
    }

    override fun bind(element: CatalogProductLoadMoreDataModel?) {
    }
}
