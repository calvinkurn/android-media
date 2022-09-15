package com.tokopedia.catalog.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.catalog.viewholder.components.CatalogAccordionItemViewHolder
import com.tokopedia.unifyprinciples.Typography

class CatalogComparisonAccordionAdapter(var subcard: ArrayList<ComponentData.SpecList.Subcard>):
    RecyclerView.Adapter<CatalogAccordionItemViewHolder>() {

    fun setData(subcardList: ArrayList<ComponentData.SpecList.Subcard>){
        subcard = subcardList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogAccordionItemViewHolder {
        return CatalogAccordionItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_subcategory, parent, false))
    }

    override fun onBindViewHolder(holder: CatalogAccordionItemViewHolder, position: Int) {
        holder.bind(subcard[position])

        when {
            subcard.size == 1 -> {
                holder.view.findViewById<Typography>(R.id.subcategory_title).setBackgroundResource(R.drawable.catalog_subtitle_background)
                holder.view.findViewById<LinearLayout>(R.id.comparison_content_layout).setBackgroundResource(R.drawable.catalog_content_border)
            }
            position == 0 -> {
                holder.view.findViewById<Typography>(R.id.subcategory_title).setBackgroundResource(R.drawable.catalog_subtitle_background)
            }
            position == subcard.size-1 -> {
                holder.view.findViewById<LinearLayout>(R.id.comparison_content_layout).setBackgroundResource(R.drawable.catalog_content_border)
            }
        }
    }

    override fun getItemCount() = subcard.size

}


