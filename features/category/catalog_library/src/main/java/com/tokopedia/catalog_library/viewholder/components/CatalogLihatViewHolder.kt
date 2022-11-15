package com.tokopedia.catalog_library.viewholder.components

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.components.CatalogLihatGridItemAdapter
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse

class CatalogLihatViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val accordionView =
        view.findViewById<AccordionUnify>(R.id.lihat_category_accordion_view)
    private var childView: View? = null

    fun bind(
        catalogLibraryData: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData,
        catalogLibraryListener: CatalogLibraryListener
    ) {
        accordionView.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = { _, isExpanded ->
                if (isExpanded) {
                    getLihatGridAdapter(
                        catalogLibraryData,
                        catalogLibraryListener
                    )?.notifyDataSetChanged()
                }
            }
        }
        childView = View.inflate(view.context, LAYOUT, null)
        childView?.findViewById<RecyclerView>(R.id.lihat_grid_view)?.apply {
            adapter = getLihatGridAdapter(catalogLibraryData, catalogLibraryListener)
            layoutManager = GridLayoutManager(view.context, COLUMN_COUNT)
        }
        getAccordionData(catalogLibraryData)
    }

    private fun getAccordionData(catalogLibraryData: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData) {
        accordionView.apply {
            childView?.let {
                val accordionDataUnify = AccordionDataUnify(
                    title = catalogLibraryData.rootCategoryName.toString(),
                    expandableView = it, isExpanded = true
                )
                addGroup(accordionDataUnify)
            }
        }
    }

    private fun getLihatGridAdapter(
        catalogLibraryData: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData,
        catalogLibraryListener: CatalogLibraryListener
    ): CatalogLihatGridItemAdapter? {
        return catalogLibraryData.childCategoryList?.let {
            CatalogLihatGridItemAdapter(it, catalogLibraryListener)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_lihat_expanded_layout
        const val COLUMN_COUNT = 4
    }
}
