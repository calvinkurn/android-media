package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogTopFiveItemViewHolder

class CatalogTopFiveAdapter(private val catalogDataList: ArrayList<CatalogListResponse.CatalogGetList.CatalogsList>, private val catalogLibraryListener: CatalogLibraryListener) :
    RecyclerView.Adapter<CatalogTopFiveItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogTopFiveItemViewHolder {
        return CatalogTopFiveItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_top_five, parent, false))
    }

    override fun onBindViewHolder(holder: CatalogTopFiveItemViewHolder, position: Int) {
        holder.bind(position + 1 , catalogDataList[position], catalogLibraryListener)
    }

    override fun getItemCount() = catalogDataList.size

}
