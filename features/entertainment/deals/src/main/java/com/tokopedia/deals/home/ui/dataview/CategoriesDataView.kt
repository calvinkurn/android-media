package com.tokopedia.deals.home.ui.dataview

import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView

/**
 * @author by jessica on 16/06/20
 */

data class CategoriesDataView(
        val list: MutableList<DealsCategoryDataView> = mutableListOf()
): DealsBaseItemDataView()