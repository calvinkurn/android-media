package com.tokopedia.catalog.adapter.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.catalog.viewholder.components.CatalogAccordionItemViewHolder
import com.tokopedia.unifyprinciples.Typography

class CatalogComparisonAccordionAdapter(var subcard: ArrayList<ComponentData.SpecList.Subcard>):
    RecyclerView.Adapter<CatalogAccordionItemViewHolder>() {

    fun setData(subCardList: ArrayList<ComponentData.SpecList.Subcard>) {
        subcard = subCardList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogAccordionItemViewHolder {
        return CatalogAccordionItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_comparision_subcategory, parent, false))
    }

    override fun onBindViewHolder(holder: CatalogAccordionItemViewHolder, position: Int) {
        holder.bind(subcard[position])
        val subcategoryTitle = holder.view.findViewById<Typography>(R.id.subcategory_title)
        val comparisonContentLayout = holder.view.findViewById<ConstraintLayout>(R.id.comparison_content_layout)
        when (position) {
            0 -> {
                subcategoryTitle.setBackgroundResource(R.drawable.catalog_subtitle_background)
            }
            subcard.size - 1 -> {
                comparisonContentLayout.setBackgroundResource(R.drawable.catalog_content_border)
            }
            else -> {
                subcategoryTitle.setBackgroundResource(R.drawable.catalog_subtitle_bg_default)
                comparisonContentLayout.setBackgroundResource(R.drawable.catalog_content_bg_default)
            }
        }
    }

    override fun getItemCount() = subcard.size
}


