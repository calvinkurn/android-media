package com.tokopedia.search.result.product.seamlessinspirationcard.utils

import com.tokopedia.search.result.product.changeview.ChangeViewListener
import com.tokopedia.search.result.product.changeview.ViewType

internal fun ChangeViewListener.isListView() = viewType.value == ViewType.LIST.value
internal fun ChangeViewListener.isBigGridView() = viewType.value == ViewType.BIG_GRID.value
internal fun ChangeViewListener.isSmallGridView() =
    viewType.value == ViewType.SMALL_GRID.value
