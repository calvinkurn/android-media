package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder

class TokoNowCategoryMenuCallback: TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener {
    override fun onCategoryMenuWidgetRetried() { /* nothing to do */ }

    override fun onSeeAllCategoryClicked() { /* nothing to do */ }

    override fun onCategoryMenuItemClicked(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) { /* nothing to do */ }

    override fun onCategoryMenuItemImpressed(
        data: TokoNowCategoryMenuItemUiModel,
        itemPosition: Int
    ) { /* nothing to do */ }

    override fun onCategoryMenuWidgetImpression(data: TokoNowCategoryMenuUiModel) { /* nothing to do */ }
}
