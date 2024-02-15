package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO

data class LoadMoreDataModel(
    val isNeededToLoadMore: Boolean,
    val counter: Int = Int.ZERO
)
