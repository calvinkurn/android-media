package com.tokopedia.catalog_library.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogLihatPageAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDataModel
import com.tokopedia.unifyprinciples.Typography

class CatalogLihatContainerViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener) :
    AbstractViewHolder<CatalogLihatDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.catalog_lihat_semua_container
    }

    override fun bind(element: CatalogLihatDataModel?) {
        if (!element?.catalogLibraryDataList.isNullOrEmpty()) {
            val catalogLihatRv = view.findViewById<RecyclerView>(R.id.lihat_category_rv)
            catalogLihatRv.apply {
                adapter = element?.catalogLibraryDataList?.let { lihatData ->
                    CatalogLihatPageAdapter(lihatData, catalogLibraryListener)
                }
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
            view.findViewById<Typography>(R.id.sort_order_0).setOnClickListener {
                catalogLibraryListener.changeSortOrderAsc()
            }
            view.findViewById<Typography>(R.id.sort_order_1).setOnClickListener {
                catalogLibraryListener.changeSortOrderDesc()
            }
        }
    }
}
