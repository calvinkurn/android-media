package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogLihatViewHolder

class CatalogLihatPageAdapter(private val catalogLibraryResponseList: ArrayList<CatalogLibraryResponse.CategoryListLibraryPage.CategoryData>,
                              private val catalogLibraryListener: CatalogLibraryListener
)
    :RecyclerView.Adapter<CatalogLihatViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLihatViewHolder {
        return CatalogLihatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_lihat_category, parent, false))
    }

    override fun onBindViewHolder(holder: CatalogLihatViewHolder, position: Int) {
        holder.bind(catalogLibraryResponseList[position], catalogLibraryListener)
    }

    override fun getItemCount() = catalogLibraryResponseList.size

}
