package com.tokopedia.catalog.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_specification.view.*

class CatalogSpecificationsAdapter (val list : ArrayList<TopSpecificationsComponentData>)
    : RecyclerView.Adapter<CatalogSpecificationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_specification, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: TopSpecificationsComponentData) {
            if(model.icon.isNullOrBlank())
                itemView.specification_iv.loadImage(CatalogConstant.DEFAULT_SPECS_ICON_URL)
            else
                itemView.specification_iv.loadImage(model.icon)
            itemView.specification_name.text = model.key
            itemView.specification_description.text = model.value
        }
    }
}