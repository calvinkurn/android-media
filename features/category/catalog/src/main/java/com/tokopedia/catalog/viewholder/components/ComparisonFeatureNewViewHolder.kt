package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.components.CatalogComparisionAccordionAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable

class ComparisonFeatureNewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
    fun bind(specList: ComponentData.SpecList, catalogDetailListener: CatalogDetailListener){
        setUpAccordionData(specList)
        specList.subcard?.let { initRecyclerView(it) }
    }

    private fun setUpAccordionData(specList: ComponentData.SpecList?){
        itemView.findViewById<AccordionUnify>(R.id.comparision_accordion_view).apply {
            addGroup(
                AccordionDataUnify(title = specList?.comparisonTitle.toString(), icon = getIconUnifyDrawable(context,
                IconUnify.CHEVRON_DOWN), expandableView = itemView.findViewById(R.id.accordion_expandable_rv), isExpanded = false)
            )

            onItemClick = {_, _ ->
                AccordionDataUnify(title = specList?.comparisonTitle.toString(), icon = getIconUnifyDrawable(context,
                    IconUnify.CHEVRON_UP), expandableView = itemView.findViewById(R.id.accordion_expandable_rv), isExpanded = true)
            }
        }
    }

    private fun initRecyclerView(subcard: ArrayList<ComponentData.SpecList.Subcard>) {
        val expandableRv = view.findViewById<RecyclerView>(R.id.accordion_expandable_rv)
        expandableRv.layoutManager = layoutManager
        expandableRv.adapter = CatalogComparisionAccordionAdapter(subcard)
    }
    companion object{
        val LAYOUT = R.layout.item_catalog_comparision_expanded_layout
    }
}