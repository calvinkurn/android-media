package com.tokopedia.catalog_library.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.viewholder.components.CatalogLihatItemViewHolder

class CatalogLihatGridItemAdapter(
    private val childCategoryList: ArrayList<CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList>,
    private val catalogLibraryListener: CatalogLibraryListener
) : RecyclerView.Adapter<CatalogLihatItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLihatItemViewHolder {
        return CatalogLihatItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_lihat_grid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CatalogLihatItemViewHolder, position: Int) {
        holder.bind(childCategoryList[position], catalogLibraryListener)
    }

    override fun getItemCount() = childCategoryList.size

}
