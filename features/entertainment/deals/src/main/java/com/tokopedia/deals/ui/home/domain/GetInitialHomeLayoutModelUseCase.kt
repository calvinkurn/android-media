package com.tokopedia.deals.ui.home.domain

import com.tokopedia.deals.common.ui.dataview.CuratedProductCategoryDataView
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.ui.home.ui.dataview.BannersDataView
import com.tokopedia.deals.ui.home.ui.dataview.CategoriesDataView

/**
 * @author by jessica on 23/06/20
 */

object GetInitialHomeLayoutModelUseCase {
    fun requestEmptyViewModels(): List<DealsBaseItemDataView> {
        return listOf(BannersDataView(),
                CategoriesDataView(),
                DealsBrandsDataView(),
                CuratedProductCategoryDataView())
    }
}
