package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogRelevantAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogRelevantDataModel

class CatalogRelevantContainerViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) :
    AbstractViewHolder<CatalogRelevantDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_relevant_container
    }

    override fun bind(element: CatalogRelevantDataModel?) {
        if (!element?.relevantDataList.isNullOrEmpty()) {
            val catalogRelevantRv = view.findViewById<RecyclerView>(R.id.catalog_relevant_rv)
            catalogRelevantRv.apply {
                adapter = element?.relevantDataList?.let { catalogRelevantList ->
                    CatalogRelevantAdapter(catalogRelevantList, catalogLibraryListener) }
                layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
}
