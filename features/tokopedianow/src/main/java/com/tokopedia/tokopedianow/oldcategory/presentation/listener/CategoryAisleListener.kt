package com.tokopedia.tokopedianow.oldcategory.presentation.listener

import com.tokopedia.tokopedianow.oldcategory.presentation.model.CategoryAisleItemDataView

interface CategoryAisleListener {
    fun onAisleClick(categoryAisleItemDataView: CategoryAisleItemDataView)
}
