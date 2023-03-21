package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogBrandCategoryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDM
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

    private val tabs: TabsUnify? by lazy(NONE) {
        itemView.findViewById(R.id.catalog_brand_tabs)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_brand_category_item
    }

    override fun bind(element: CatalogBrandCategoryDM?) {
        dataModel = element
        tabs?.hasRightArrow = true
        element?.catalogLibraryDataList?.forEach {
            tabs?.addNewTab(it?.rootCategoryName ?: "")
        }

        tabs?.getUnifyTabLayout()?.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                catalogLibraryListener.onBrandCategoryTabSelected(
                    element?.catalogLibraryDataList?.get(tab?.position ?: 0)?.rootCategoryName ?: "",
                    element?.catalogLibraryDataList?.get(tab?.position ?: 0)?.rootCategoryId ?: ""
                )
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        tabs?.arrowView?.setOnClickListener {
            catalogLibraryListener.onBrandCategoryArrowClick()
        }
    }

    override fun onViewAttachedToWindow() {

    }
}
