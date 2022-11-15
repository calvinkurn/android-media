package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogRelevantItemViewHolder

class CatalogRelevantAdapter(var catalogRelevantResponseData: ArrayList<CatalogRelevantResponse.Catalogs>, private val catalogLibraryListener: CatalogLibraryListener):
    RecyclerView.Adapter<CatalogRelevantItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogRelevantItemViewHolder {
        return CatalogRelevantItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_relevant, parent, false))
    }

    override fun onBindViewHolder(holder: CatalogRelevantItemViewHolder, position: Int) {
        holder.bind(catalogRelevantResponseData[position], catalogLibraryListener)
    }

    override fun getItemCount() = catalogRelevantResponseData.size

}

