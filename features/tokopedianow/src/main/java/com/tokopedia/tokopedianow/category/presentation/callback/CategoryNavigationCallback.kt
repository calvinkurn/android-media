package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder

class CategoryNavigationCallback : CategoryNavigationViewHolder.CategoryNavigationListener {
    override fun onCategoryNavigationWidgetRetried() { /* nothing to do */ }

    override fun onCategoryNavigationItemClicked(
        data: CategoryNavigationItemUiModel,
        itemPosition: Int
    ) { /* nothing to do temp */ }

    override fun onCategoryNavigationItemImpressed(
        data: CategoryNavigationItemUiModel,
        itemPosition: Int
    ) { /* nothing to do temp */ }

    override fun onCategoryNavigationWidgetImpression(data: CategoryNavigationUiModel) { /* nothing to do temp */ }
}

