package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryNavigationViewHolder

class CategoryNavigationCallback(
    private val onClickCategoryNavigation: (categoryIdL2: String) -> Unit,
    private val onImpressCategoryNavigation: (categoryIdL2: String) -> Unit
) : CategoryNavigationViewHolder.CategoryNavigationListener {
    override fun onCategoryNavigationItemClicked(
        data: CategoryNavigationItemUiModel,
        itemPosition: Int
    ) = onClickCategoryNavigation(data.id)

    override fun onCategoryNavigationItemImpressed(
        data: CategoryNavigationItemUiModel,
        itemPosition: Int
    ) = onImpressCategoryNavigation(data.id)

    override fun onCategoryNavigationWidgetRetried() { /* nothing to do */ }

    override fun onCategoryNavigationWidgetImpression(data: CategoryNavigationUiModel) { /* nothing to do temp */ }
}

