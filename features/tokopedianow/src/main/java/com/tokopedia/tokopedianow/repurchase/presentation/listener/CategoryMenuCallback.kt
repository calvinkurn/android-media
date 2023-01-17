package com.tokopedia.tokopedianow.repurchase.presentation.listener

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.repurchase.presentation.viewmodel.TokoNowRepurchaseViewModel

class CategoryMenuCallback(
    private val analytics: RepurchaseAnalytics,
    private val viewModel: TokoNowRepurchaseViewModel,
): TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {

    override fun onCategoryMenuWidgetRetried() {
        viewModel.getCategoryMenu()
    }

    override fun onSeeAllCategoriesClicked() {
        analytics.trackClickSeeAllCategories()
    }

    override fun onCategoryMenuItemClicked(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) {
        analytics.trackClickCategoryMenu(
            categoryId = data.id,
            categoryName = data.title,
            warehouseId = viewModel.warehouseId,
            position = itemPosition
        )
    }

    override fun onCategoryMenuItemImpressed(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) {
        analytics.trackImpressCategoryMenu(
            categoryId = data.id,
            categoryName = data.title,
            warehouseId = viewModel.warehouseId,
            position = itemPosition
        )
    }

    override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { /* nothing to do */ }
}
