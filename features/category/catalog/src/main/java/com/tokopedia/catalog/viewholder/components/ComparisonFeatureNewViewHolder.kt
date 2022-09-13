package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.components.CatalogComparisonAccordionAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComponentData

class ComparisonFeatureNewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val accordionView = view.findViewById<AccordionUnify>(R.id.comparision_accordion_view)
    private var childView: View? = null

    fun bind(specList: ComponentData.SpecList, catalogDetailListener: CatalogDetailListener){
        val catalogComparisonAccordionAdapter = specList.subcard?.let {
            CatalogComparisonAccordionAdapter(it)
        }
        accordionView.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = {_, isExpanded ->

                if(isExpanded){
                    catalogComparisonAccordionAdapter?.setData(specList.subcard)
                }
            }
        }
        childView = View.inflate(view.context, LAYOUT, null)
        childView?.findViewById<RecyclerView>(R.id.accordion_expandable_rv)?.apply {
            adapter = catalogComparisonAccordionAdapter
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        }

        addAccordionData(specList)
    }

    private fun addAccordionData(specList: ComponentData.SpecList?){
        accordionView.apply {
            addGroup(AccordionDataUnify(title = specList?.comparisonTitle.toString(), expandableView = childView!!))
            }
        }

    companion object{
        val LAYOUT = R.layout.item_catalog_comparision_expanded_layout
    }
}