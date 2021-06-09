package com.tokopedia.catalog.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogComparisionAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogComparisionDataModel

class CatalogComparisionContainerViewHolder(private val view : View,
                                       private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogComparisionDataModel>(view) {

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_comparision_container
    }

    override fun bind(element: CatalogComparisionDataModel) {
        val comparisionRV = view.findViewById<RecyclerView>(R.id.catalog_comparision_rv)
        comparisionRV.layoutManager = layoutManager
        comparisionRV.adapter = CatalogComparisionAdapter(ArrayList<String>(element.keySet),element.baseCatalog,element.comparisionCatalog,catalogDetailListener)
    }
}