package com.tokopedia.oldcatalog.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.adapter.components.CatalogComparisonNewAdapter
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogComparisonNewDataModel

class CatalogComparisonContainerNewViewHolder(val view : View,
                                              val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogComparisonNewDataModel>(view) {

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_comparision_new_container
    }

    override fun bind(element: CatalogComparisonNewDataModel) {
        if(element.specsList?.isNullOrEmpty() == false){
            val comparisonRV = view.findViewById<RecyclerView>(R.id.catalog_comparision_rv)
            comparisonRV.layoutManager = layoutManager
            comparisonRV.adapter = CatalogComparisonNewAdapter(element.specsList, catalogDetailListener)
        }
    }
}
