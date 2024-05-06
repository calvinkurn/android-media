package com.tokopedia.tokopedianow.annotation.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class LoadMoreDataModel(
    val isNeededToLoadMore: Boolean,
    val pageLastId: String = String.EMPTY
)
