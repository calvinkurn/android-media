package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogBrandCategoryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDM
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.util.CatalogAnalyticsLihatSemuaPage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlin.LazyThreadSafetyMode.NONE

class CatalogBrandCategoryItemVH(
    val view: View,
    private val catalogLibraryListener:
        CatalogLibraryListener
) : AbstractViewHolder<CatalogBrandCategoryDM>(view) {

    var dataModel: CatalogBrandCategoryDM? = null
    private val semua = CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList(
     "","Semua"
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
        tabSelectedListener?.let {
            tabs?.tabLayout?.removeAllTabs()
            tabs?.tabLayout?.removeOnTabSelectedListener(tabSelectedListener)
        }
        tabs?.hasRightArrow = true
        categoryItems = arrayListOf(semua)
        tabs?.addNewTab(semua.categoryName ?: "",dataModel?.selectedCategoryId == semua.categoryId)
        dataModel?.catalogLibraryDataList?.forEach { category ->
            category.childCategoryList?.forEach { childCategory ->
                categoryItems.add(childCategory)
                tabs?.addNewTab(childCategory.categoryName ?: "",
                    (dataModel?.selectedCategoryId == childCategory.categoryId))
            }
        }

        tabSelectedListener = object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(categoryItems.size > tab?.position ?: 0 && dataModel?.selectedCategoryId != categoryItems[tab?.position ?: 0].categoryId){
                    catalogLibraryListener.onBrandCategoryTabSelected(
                        categoryItems[tab?.position ?: 0].categoryName ?: "",
                        categoryItems[tab?.position ?: 0].categoryId ?: "",
                    )
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        }

        tabs?.getUnifyTabLayout()?.addOnTabSelectedListener(tabSelectedListener)

        tabs?.arrowView?.setOnClickListener {
            catalogLibraryListener.onBrandCategoryArrowClick()
        }
    }

    override fun onViewAttachedToWindow() {

    }
}
