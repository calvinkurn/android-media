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

    fun bind(specList: ComponentData.SpecList, catalogDetailListener: CatalogDetailListener) {
        accordionView.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = { _, isExpanded ->

                if (isExpanded) {
                    specList.subcard?.let {
                        getAccordionAdapter(specList)?.setData(specList.subcard)
                        catalogDetailListener.accordionDropDown(specList.comparisonTitle)
                    }
                } else {
                    catalogDetailListener.accordionDropUp(specList.comparisonTitle)
                }
            }
        }
        childView = View.inflate(view.context, LAYOUT, null)
        childView?.findViewById<RecyclerView>(R.id.accordion_expandable_rv)?.apply {
            if (specList.subcard != null) {
                adapter = getAccordionAdapter(specList)
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
        addAccordionData(specList)
    }

    private fun addAccordionData(specList: ComponentData.SpecList?) {
        accordionView.apply {
            childView?.let {
                val accordionUnifyData = AccordionDataUnify(
                    title = specList?.comparisonTitle.toString(),
                    expandableView = it,
                    isExpanded = specList?.isExpanded ?: false
                )
                accordionUnifyData.setContentPadding(0, 0, 0, 16)
                addGroup(accordionUnifyData)
            }
        }
    }

    private fun getAccordionAdapter(specList: ComponentData.SpecList): CatalogComparisonAccordionAdapter? {
        return specList.subcard?.let {
            CatalogComparisonAccordionAdapter(it)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_comparision_expanded_layout
    }
}