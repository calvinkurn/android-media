package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogSpecialItemViewHolder

class CatalogSpecialAdapter(
    private val catalogSpecialData: ArrayList<CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialData>,
    private val catalogLibraryListener: CatalogLibraryListener
) :
    RecyclerView.Adapter<CatalogSpecialItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogSpecialItemViewHolder {
        return CatalogSpecialItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_special, parent, false))
    }

    override fun onBindViewHolder(holder: CatalogSpecialItemViewHolder, position: Int) {
        holder.bind(catalogSpecialData[position], catalogLibraryListener)
    }

    override fun getItemCount() = catalogSpecialData.size
}

