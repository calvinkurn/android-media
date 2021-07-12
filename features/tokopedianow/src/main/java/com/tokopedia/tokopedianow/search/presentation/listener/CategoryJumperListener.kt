package com.tokopedia.tokopedianow.search.presentation.listener

import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView

interface CategoryJumperListener {

    fun onCategoryJumperItemClick(item: CategoryJumperDataView.Item)
}