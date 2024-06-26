package com.tokopedia.oldcatalog.adapter.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.oldcatalog.viewholder.components.SpecificationsViewHolder

class CatalogSpecificationsAdapter (val list : ArrayList<TopSpecificationsComponentData>,
                                    private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<SpecificationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationsViewHolder {
        return SpecificationsViewHolder(View.inflate(parent.context, R.layout.item_catalog_specification, null))
    }

    override fun getItemCount(): Int  = (list.size + 1)

    override fun onBindViewHolder(holder: SpecificationsViewHolder, position: Int) {
        if(position == list.size)
            holder.bind(null,catalogDetailListener)
        else
            holder.bind(list[position],catalogDetailListener)
    }
}
