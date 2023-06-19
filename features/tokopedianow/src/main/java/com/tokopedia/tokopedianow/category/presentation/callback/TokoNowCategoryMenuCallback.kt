package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder

class TokoNowCategoryMenuCallback(
    private val onClickCategoryMenu: (categoryRecomIdL1: String) -> Unit,
    private val onImpressCategoryMenu: (categoryIdL1: String) -> Unit
): TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
    override fun onCategoryMenuWidgetRetried() { /* nothing to do */ }

    override fun onSeeAllCategoryClicked() { /* nothing to do */ }

    override fun onCategoryMenuItemClicked(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) = onClickCategoryMenu(data.id)

    override fun onCategoryMenuItemImpressed(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) = onImpressCategoryMenu(data.id)

    override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { /* nothing to do */ }
}
