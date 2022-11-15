package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogSpecialAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogSpecialDataModel
import com.tokopedia.unifyprinciples.Typography

class CatalogSpecialContainerViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) :
    AbstractViewHolder<CatalogSpecialDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_special_container
        const val COLUMN_COUNT = 4
    }

    override fun bind(element: CatalogSpecialDataModel?) {
        if (!element?.specialDataList.isNullOrEmpty()) {
            val gridView = view.findViewById<RecyclerView>(R.id.catalog_special_grid_view)
            gridView.apply {
                adapter = element?.specialDataList?.let { catalogSpecialList ->
                    CatalogSpecialAdapter(catalogSpecialList, catalogLibraryListener)
                }
                layoutManager = GridLayoutManager(view.context, COLUMN_COUNT)
            }
            view.findViewById<Typography>(R.id.lihat_semua_text).setOnClickListener {
                catalogLibraryListener.onLihatSemuaTextClick()
            }
        }
    }
}


