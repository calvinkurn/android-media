package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogMostViralAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogMostViralDataModel

class CatalogMostViralContainerViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) :
    AbstractViewHolder<CatalogMostViralDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_most_viral_container
    }

    override fun bind(element: CatalogMostViralDataModel?) {
        if (!element?.catalogMostViralData.isNullOrEmpty()){
            val catalogMostViralRv = view.findViewById<RecyclerView>(R.id.catalog_most_viral_rv)
            catalogMostViralRv.apply {
                adapter = element?.catalogMostViralData?.let { CatalogMostViralAdapter(it, catalogLibraryListener) }
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
    }
}
