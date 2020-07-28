package com.tokopedia.deals.home.ui.dataview

import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView

/**
 * @author by jessica on 24/06/20
 */

data class CuratedCategoryDataView(
        val title: String = "",
        val subtitle: String = "",
        val curatedCategories: MutableList<CuratedCategory> = mutableListOf()
): DealsBaseItemDataView() {
    data class CuratedCategory(
            val id: String = "",
            val imageUrl: String = "",
            val name: String = "",
            val url: String = ""
    )
}