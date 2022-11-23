package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogListAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLandingListDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogListDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel

class CatalogShimmerViewHolder(val view: View) :
    AbstractViewHolder<CatalogShimmerDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_shimmer_catalog_specifications_container
    }

    override fun bind(element: CatalogShimmerDataModel?) {

    }
}
