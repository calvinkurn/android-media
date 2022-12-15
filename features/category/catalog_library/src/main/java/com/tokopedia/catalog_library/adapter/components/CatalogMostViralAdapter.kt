package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogMostViralItemViewHolder

class CatalogMostViralAdapter(private val catalogDataList: ArrayList<CatalogListResponse.CatalogGetList.CatalogsList>, private val catalogLibraryListener: CatalogLibraryListener) :
    RecyclerView.Adapter<CatalogMostViralItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogMostViralItemViewHolder {
        return CatalogMostViralItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_catalog_most_viral, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CatalogMostViralItemViewHolder, position: Int) {
        holder.bind(catalogDataList[position], catalogLibraryListener)
    }

    override fun getItemCount() = catalogDataList.size
}
