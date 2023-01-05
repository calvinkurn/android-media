package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogListAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLandingListDataModel

class CatalogLandingPageListContainerViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) :
    AbstractViewHolder<CatalogLandingListDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.catalog_landing_page_list_container
    }

    override fun bind(element: CatalogLandingListDataModel?) {
        if (!element?.catalogProductList.isNullOrEmpty()) {
            view.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_landing_page_list_header).text = "Semua katalog ${element?.categoryName}"
            val catalogListRv = view.findViewById<RecyclerView>(R.id.catalog_landing_page_list_rv)
            catalogListRv.apply {
                adapter = element?.catalogProductList?.let { catalogProductList ->
                    CatalogListAdapter(catalogProductList, catalogLibraryListener)
                }
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
    }
}
