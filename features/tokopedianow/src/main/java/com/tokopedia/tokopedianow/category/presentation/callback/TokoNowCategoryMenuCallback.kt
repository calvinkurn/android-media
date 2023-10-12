package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder

class TokoNowCategoryMenuCallback(
    private val onClickCategoryMenu: (categoryRecomIdL1: String, headerName: String) -> Unit,
    private val onImpressCategoryMenu: (categoryIdL1: String, headerName: String) -> Unit
): TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
    override fun onCategoryMenuWidgetRetried() { /* nothing to do */ }

    override fun onSeeAllCategoryClicked() { /* nothing to do */ }

    override fun onCategoryMenuItemClicked(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) = onClickCategoryMenu(data.id, data.headerName)

    override fun onCategoryMenuItemImpressed(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) = onImpressCategoryMenu(data.id, data.headerName)

    override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { /* nothing to do */ }
}
