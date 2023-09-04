package com.tokopedia.tokopedianow.category.presentation.view

import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateModel

interface CategoryL2MainView {

    fun onShowEmptyState(
        model: CategoryEmptyStateModel,
        filterController: FilterController
    )

    fun onHideEmptyState()
}
