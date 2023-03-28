package com.tokopedia.catalog_library.viewholder

import android.os.Handler
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogBrandCategoryDM
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.unifycomponents.TabsUnify
import kotlin.LazyThreadSafetyMode.NONE

class CatalogBrandCategoryItemVH(
    val view: View,
    private val catalogLibraryListener:
        CatalogLibraryListener
) : CatalogLibraryAbstractViewHolder<CatalogBrandCategoryDM>(view), TabLayout.OnTabSelectedListener {

    var dataModel: CatalogBrandCategoryDM? = null
    private val semua = CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList(
        "",
        "Semua"
    )
    private var categoryItems = ArrayList<CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList>()

    private val tabs: TabsUnify? by lazy(NONE) {
        itemView.findViewById(R.id.catalog_brand_tabs)
    }

    var tabSelectedListener: TabLayout.OnTabSelectedListener? = null

    companion object {
        val LAYOUT = R.layout.item_catalog_brand_category_item
    }

    override fun bind(element: CatalogBrandCategoryDM?) {
        dataModel = element
        tabs?.tabLayout?.removeAllTabs()
        tabSelectedListener?.let {
            // tabs?.tabLayout?.removeOnTabSelectedListener(tabSelectedListener)
        }
        tabs?.hasRightArrow = true
        var selectedIndex = 0
        categoryItems = arrayListOf(semua)
        tabs?.addNewTab(semua.categoryName ?: "", dataModel?.selectedCategoryId == semua.categoryId)
        dataModel?.catalogLibraryDataList?.forEach { category ->
            category.childCategoryList?.forEach { childCategory ->
                val isSelected = (dataModel?.selectedCategoryId == childCategory.categoryId)
                categoryItems.add(childCategory)
                tabs?.addNewTab(
                    childCategory.categoryName ?: "",
                    setSelected =
                    isSelected
                )
                if (isSelected) selectedIndex = categoryItems.size - 1
            }
        }

        Handler().postDelayed({
            tabs?.tabLayout?.getTabAt(selectedIndex)?.select()
        }, 100)

        tabs?.arrowView?.setOnClickListener {
            catalogLibraryListener.onBrandCategoryArrowClick()
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        tabs?.getUnifyTabLayout()?.addOnTabSelectedListener(this)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        catalogLibraryListener.onBrandCategoryTabSelected(
            categoryItems[tab?.position ?: 0].categoryName ?: "",
            categoryItems[tab?.position ?: 0].categoryId ?: ""
        )
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow()
        tabs?.getUnifyTabLayout()?.removeOnTabSelectedListener(this)
    }
}
