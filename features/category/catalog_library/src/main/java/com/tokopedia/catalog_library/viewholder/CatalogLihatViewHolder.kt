package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDataModel
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse.CategoryListLibraryPage.CategoryData
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_LIHAT_SEMUA_ITEM
import com.tokopedia.unifyprinciples.Typography

class CatalogLihatViewHolder(
    private val view: View,
    private val catalogLibraryListener: CatalogLibraryListener,
    private val sharedRecycledViewPool: RecycledViewPool
) : AbstractViewHolder<CatalogLihatDataModel>(view) {

    private val accordionView: AccordionUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.lihat_category_accordion_view)
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            catalogLibraryListener
        )
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_lihat_category
        const val COLUMN_COUNT = 4
    }

    override fun bind(element: CatalogLihatDataModel) {
        accordionView.run {
            accordionData.clear()
            removeAllViews()
        }
        getAccordionData(element.catalogLibraryDataList)
    }

    private fun getAccordionData(catalogLibraryData: CategoryData?) {
        accordionView.apply {
            addGroup(
                setExpandableChildView(catalogLibraryData)
            )
        }
    }

    private fun setExpandableChildView(catalogLibraryData: CategoryData?): AccordionDataUnify {
        val listAdapter = CatalogLibraryAdapter(
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build(),
            catalogLibraryAdapterFactory
        )
        val expandableLayout =
            View.inflate(itemView.context, R.layout.item_catalog_lihat_expanded_layout, null)
        expandableLayout.findViewById<RecyclerView>(R.id.lihat_grid_view).apply {
            setRecycledViewPool(sharedRecycledViewPool)
            adapter = listAdapter
            layoutManager = GridLayoutManager(view.context, COLUMN_COUNT)
        }

        listAdapter.submitList(getChildVisitableList(catalogLibraryData?.childCategoryList))
        return AccordionDataUnify(
            title = catalogLibraryData?.rootCategoryName.toString(),
            expandableView = expandableLayout,
            isExpanded = true
        )
    }

    private fun getChildVisitableList(childCategoryList: ArrayList<CategoryData.ChildCategoryList>?)
        : MutableList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        childCategoryList?.forEach {
            visitableList.add(
                CatalogLihatItemDataModel(
                    CATALOG_LIHAT_SEMUA_ITEM,
                    CATALOG_LIHAT_SEMUA_ITEM,
                    it
                )
            )
        }
        return visitableList
    }
}
