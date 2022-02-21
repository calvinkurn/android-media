package com.tokopedia.catalog.viewholder.products

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.components.CatalogForYouAdapter
import com.tokopedia.catalog.listener.CatalogProductCardListener

class CatalogForYouContainerViewHolder(private val view : View,
                                       private val catalogProductCardListener: CatalogProductCardListener?
): AbstractViewHolder<CatalogForYouContainerDataModel>(view) {

    private val myLayoutManger = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_for_you_container
    }

    override fun bind(element: CatalogForYouContainerDataModel) {
        view.findViewById<RecyclerView>(R.id.catalog_for_you_rv)?.apply {
            layoutManager = myLayoutManger
            adapter = CatalogForYouAdapter(element.catalogProductList?.catalogComparisonList, catalogProductCardListener)
        }
   }
}