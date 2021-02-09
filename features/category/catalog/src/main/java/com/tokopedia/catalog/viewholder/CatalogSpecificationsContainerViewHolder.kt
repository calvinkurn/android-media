package com.tokopedia.catalog.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogSpecificationsAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel

class CatalogSpecificationsContainerViewHolder(private val view : View,
                                               private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogSpecificationDataModel>(view) {

    private var specificationsAdapter : CatalogSpecificationsAdapter? = null
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_specifications_container
    }

    override fun bind(element: CatalogSpecificationDataModel) {
        val specificationsRV = view.findViewById<RecyclerView>(R.id.catalog_specification_rv)
        specificationsRV.layoutManager = layoutManager
        specificationsAdapter = CatalogSpecificationsAdapter(element.specificationsList, catalogDetailListener)
        specificationsRV.adapter = specificationsAdapter!!
    }
}