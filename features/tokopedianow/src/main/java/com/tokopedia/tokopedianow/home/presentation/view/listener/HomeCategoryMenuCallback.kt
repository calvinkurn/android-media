package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class HomeCategoryMenuCallback(
    private val analytics: HomeAnalytics,
    private val localCacheModel: LocalCacheModel?,
    private val viewModel: TokoNowHomeViewModel
) : TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
    override fun onCategoryMenuWidgetRetried() {
        localCacheModel?.let {
            viewModel.getCategoryMenu(it)
        }
    }

    override fun onSeeAllCategoryClicked() {
        analytics.onClickAllCategory()
    }

    override fun onCategoryMenuItemClicked(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) {
        analytics.onClickCategory(itemPosition, data.id, data.headerName, data.title)
    }

    override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) {
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        analytics.trackCategoryImpression(data, warehouseId)
    }

    override fun onCategoryMenuItemImpressed(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) { /* nothing to do */ }
}
