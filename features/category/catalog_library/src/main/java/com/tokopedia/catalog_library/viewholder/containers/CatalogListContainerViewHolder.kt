package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogListAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogListDataModel

class CatalogListContainerViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) :
    AbstractViewHolder<CatalogListDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_list_container
    }

    override fun bind(element: CatalogListDataModel?) {
        if (!element?.catalogProductList.isNullOrEmpty()) {
            val catalogListRv = view.findViewById<RecyclerView>(R.id.catalog_list_rv)
            catalogListRv.apply {
                adapter = element?.catalogProductList?.let { catalogProductList ->
                    CatalogListAdapter(catalogProductList, catalogLibraryListener)
                }
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
    }
}

