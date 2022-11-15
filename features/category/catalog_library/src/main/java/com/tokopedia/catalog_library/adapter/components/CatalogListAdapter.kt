package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogListItemViewHolder

class CatalogListAdapter(private val catalogListResponseData: ArrayList<CatalogListResponse.CatalogGetList.CatalogsList>, private val catalogLibraryListener: CatalogLibraryListener) :
    RecyclerView.Adapter<CatalogListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogListItemViewHolder {
        return CatalogListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CatalogListItemViewHolder, position: Int) {
        holder.bind(catalogListResponseData[position], catalogLibraryListener)
    }

    override fun getItemCount() = catalogListResponseData.size

}
