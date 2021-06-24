package com.tokopedia.tokomart.category.presentation.listener

import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView

interface CategoryAisleListener {

    fun onAisleClick(categoryAisleItemDataView: CategoryAisleItemDataView)
}