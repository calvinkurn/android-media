package com.tokopedia.catalog.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.components.CatalogComparisonNewAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogComparisionNewDataModel

class CatalogComparisonContainerNewViewHolder(private val view : View,
                                              private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogComparisionNewDataModel>(view) {

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_comparision_new_container
    }

    override fun bind(element: CatalogComparisionNewDataModel) {
        if(element.specsList?.isNullOrEmpty() == false){
            val comparisonRV = view.findViewById<RecyclerView>(R.id.catalog_comparision_rv)
            comparisonRV.layoutManager = layoutManager
            comparisonRV.adapter = CatalogComparisonNewAdapter(element.specsList, catalogDetailListener)
        }
    }
}