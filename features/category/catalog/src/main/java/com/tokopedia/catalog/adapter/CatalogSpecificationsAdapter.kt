package com.tokopedia.catalog.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.TopSpecificationsComponentData
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_specification.view.*

class CatalogSpecificationsAdapter (val list : ArrayList<TopSpecificationsComponentData>,
                                    private val catalogDetailListener: CatalogDetailListener)
    : RecyclerView.Adapter<CatalogSpecificationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_specification, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], catalogDetailListener)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: TopSpecificationsComponentData, catalogDetailListener: CatalogDetailListener?) {
            itemView.specification_iv.loadImage(model.icon ?: "")
            itemView.specification_name.text = model.key
            itemView.specification_description.text = model.value
            itemView.setOnClickListener {

            }
        }
    }
}