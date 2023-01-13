package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_LIHAT_SEMUA_ITEM

class CatalogLihatViewHolder(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener,
    private val customeRecycledViewPool: RecycledViewPool
): AbstractViewHolder<CatalogLihatDataModel>(view){

    private val accordionView =
        view.findViewById<AccordionUnify>(R.id.lihat_category_accordion_view)
    private var childView: View? = null
//    private var childView: View? = View.inflate(view.context, LAYOUT_ACCORDION, null)


    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            catalogLibraryListener
        )
    }
    private val listAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDataModel> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_lihat_category
        val LAYOUT_ACCORDION = R.layout.item_catalog_lihat_expanded_layout
        const val COLUMN_COUNT = 4
    }

    override fun bind(element: CatalogLihatDataModel) {
        accordionView.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = { _, isExpanded ->
                if (isExpanded) {
                    listAdapter.submitList(getChildVisitableList(element.catalogLibraryDataList?.childCategoryList))
                }
            }
        }
        childView = View.inflate(view.context, LAYOUT_ACCORDION, null)
        if(childView != null)
        {
            childView?.findViewById<RecyclerView>(R.id.lihat_grid_view)?.apply {
                setRecycledViewPool(customeRecycledViewPool)
                adapter = listAdapter
                layoutManager = GridLayoutManager(view.context, COLUMN_COUNT)
            }
            listAdapter.submitList(getChildVisitableList(element.catalogLibraryDataList?.childCategoryList))
        }

        getAccordionData(element.catalogLibraryDataList)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
    }

    private fun getChildVisitableList(childCategoryList: ArrayList<CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList>?): MutableList<BaseCatalogLibraryDataModel>? {
        val visitables = arrayListOf<BaseCatalogLibraryDataModel>()
        childCategoryList?.forEach {
            visitables.add(CatalogLihatItemDataModel(CATALOG_LIHAT_SEMUA_ITEM,CATALOG_LIHAT_SEMUA_ITEM,it))
        }
        return visitables
    }

    private fun getAccordionData(catalogLibraryData: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData?) {
        accordionView.apply {
            childView?.let {
                val accordionDataUnify = AccordionDataUnify(
                    title = catalogLibraryData?.rootCategoryName.toString(),
                    expandableView = it, isExpanded = true
                )
                addGroup(accordionDataUnify)
            }
        }
    }
}
