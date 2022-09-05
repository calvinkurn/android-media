package com.tokopedia.catalog.adapter.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.catalog.viewholder.components.CatalogAccordionViewHolder

class CatalogComparisionAccordionAdapter(val subcard: ArrayList<ComponentData.SpecList.Subcard>):
    RecyclerView.Adapter<CatalogAccordionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogAccordionViewHolder {
        return CatalogAccordionViewHolder(View.inflate(parent.context, R.layout.item_catalog_comparision_subcategory, null))
    }

    override fun onBindViewHolder(holder: CatalogAccordionViewHolder, position: Int) {
        holder.bind(subcard[position])
    }

    override fun getItemCount() = subcard.size

}


