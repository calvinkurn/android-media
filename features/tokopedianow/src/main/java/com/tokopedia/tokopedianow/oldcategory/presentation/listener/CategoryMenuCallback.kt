package com.tokopedia.tokopedianow.oldcategory.presentation.listener

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking
import com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel.TokoNowCategoryViewModel

class CategoryMenuCallback(
    private val viewModel: TokoNowCategoryViewModel,
    private val userId: String
): TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {

    override fun onCategoryMenuWidgetRetried() {
        viewModel.onCategoryMenuRetry()
    }

    override fun onSeeAllCategoryClicked() {
        CategoryTracking.trackClickSeeAllCategory()
    }

    override fun onCategoryMenuItemClicked(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) {
        CategoryTracking.trackClickCategoryMenu(
            categoryId = data.id,
            categoryName = data.title,
            warehouseId = viewModel.warehouseId,
            position = itemPosition,
            userId = userId
        )
    }

    override fun onCategoryMenuItemImpressed(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) {
        CategoryTracking.trackImpressCategoryMenu(
            categoryId = data.id,
            categoryName = data.title,
            warehouseId = viewModel.warehouseId,
            position = itemPosition,
            userId = userId
        )
    }

    override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { /* nothing to do */ }
}
