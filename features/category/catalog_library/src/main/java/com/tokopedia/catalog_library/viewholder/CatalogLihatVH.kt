package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.CatalogLibraryGridSpacingItemDecoration
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDM
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDM
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatListItemDM
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse.CategoryListLibraryPage.CategoryData
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_LIHAT_SEMUA_ITEM

class CatalogLihatVH(
    private val view: View,
    private val catalogLibraryListener: CatalogLibraryListener,
    private val sharedRecycledViewPool: RecycledViewPool
) : CatalogLibraryAbstractViewHolder<CatalogLihatDM>(view) {

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

    override fun bind(element: CatalogLihatDM) {
        accordionView.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = { _, isExpanded ->
                catalogLibraryListener.onAccordionStateChange(isExpanded, element)
                element.catalogLibraryDataList?.accordionExpandedState = isExpanded
            }
        }
        getAccordionData(element.catalogLibraryDataList, element.isAsc, element.isTypeList, element.activeCategoryId)
    }

    private fun getAccordionData(catalogLibraryData: CategoryData?, isAsc: Boolean, isTypeList: Boolean, activeCategoryId: String) {
        accordionView.apply {
            addGroup(
                setExpandableChildView(catalogLibraryData, isAsc, isTypeList, activeCategoryId)
            )
        }
    }

    private fun setExpandableChildView(
        catalogLibraryData: CategoryData?,
        isAsc: Boolean,
        isTypeList: Boolean,
        activeCategoryId: String
    ): AccordionDataUnify {
        val listAdapter = CatalogLibraryAdapter(
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build(),
            catalogLibraryAdapterFactory
        )
        val expandableLayout =
            View.inflate(itemView.context, R.layout.item_catalog_lihat_expanded_layout, null)
        expandableLayout.findViewById<RecyclerView>(R.id.lihat_grid_view).apply {
            setRecycledViewPool(sharedRecycledViewPool)
            adapter = listAdapter
            layoutManager = if (isTypeList) {
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            } else {
                addItemDecoration(CatalogLibraryGridSpacingItemDecoration(COLUMN_COUNT, 0, false))
                GridLayoutManager(view.context, COLUMN_COUNT)
            }
        }

        listAdapter.submitList(
            getChildVisitableList(
                catalogLibraryData?.childCategoryList,
                catalogLibraryData?.rootCategoryName,
                catalogLibraryData?.rootCategoryId,
                isAsc,
                isTypeList,
                activeCategoryId
            )
        )
        return AccordionDataUnify(
            title = catalogLibraryData?.rootCategoryName.toString(),
            expandableView = expandableLayout,
            isExpanded = catalogLibraryData?.accordionExpandedState ?: true
        )
    }

    private fun getChildVisitableList(
        childCategoryList: ArrayList<CategoryData.ChildCategoryList>?,
        rootCategoryName: String?,
        rootCategoryId: String?,
        isAsc: Boolean,
        isTypeList: Boolean,
        activeCategoryId: String
    ): MutableList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        childCategoryList?.forEachIndexed { index, catalog ->
            if (isTypeList) {
                visitableList.add(
                    CatalogLihatListItemDM(
                        CATALOG_LIHAT_SEMUA_ITEM,
                        CATALOG_LIHAT_SEMUA_ITEM,
                        catalog,
                        rootCategoryId ?: "",
                        rootCategoryName ?: "",
                        false,
                        isAsc,
                        activeCategoryId,
                        index == (childCategoryList.size - 1)
                    )
                )
            } else {
                visitableList.add(
                    CatalogLihatItemDM(
                        CATALOG_LIHAT_SEMUA_ITEM,
                        CATALOG_LIHAT_SEMUA_ITEM,
                        catalog,
                        rootCategoryId ?: "",
                        rootCategoryName ?: "",
                        true,
                        isAsc
                    )
                )
            }
        }
        return visitableList
    }
}
