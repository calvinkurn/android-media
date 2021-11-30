package com.tokopedia.tokopedianow.category.presentation.listener

import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleItemDataView

interface CategoryAisleListener {

    fun onAisleClick(categoryAisleItemDataView: CategoryAisleItemDataView)
}