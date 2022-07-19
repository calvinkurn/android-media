package com.tokopedia.feedcomponent.view.widget.shoprecom.utils

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.view.widget.shoprecom.adapter.ShopRecomAdapter


fun LinearLayoutManager.getVisibleItems(adapter: ShopRecomAdapter): List<Pair<ShopRecomUiModelItem, Int>> {
    val items = adapter.getItems()
    val (start, end) = getVisibleItemsPosition(this, adapter)

    if (start > -1 && end < items.size && start <= end) {
        return items.slice(start..end)
            .mapIndexed { index, item ->
                Pair(item, start + index)
            }
    }

    return emptyList()
}

private fun getVisibleItemsPosition(
    layoutManager: LinearLayoutManager,
    adapter: ShopRecomAdapter,
): Pair<Int, Int> {

    val products = adapter.getItems()
    if (products.isNotEmpty()) {
        val startPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val endPosition = layoutManager.findLastVisibleItemPosition()

        return Pair(startPosition, endPosition)
    }

    return Pair(-1, -1)
}