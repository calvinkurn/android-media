package com.tokopedia.search.result.product.visitable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.product.productitem.ProductItemVisitable

fun List<Visitable<*>>.indexOfFirstProductItem(
    predicate: (ProductItemVisitable) -> Boolean = { true }
) = indexOfFirst { it is ProductItemVisitable && it.isCountedAsProductItem() && predicate(it) }

fun List<Visitable<*>>.getTotalProductItem() =
    filter { it.isCountedAsProductItem() }.size

fun List<Visitable<*>>.getIndexForWidgetPosition(widgetPosition: Int): Int {
    var productItemIndex = 0

    forEachIndexed { i, visitable ->
        if (visitable.isCountedAsProductItem()) productItemIndex++
        if (productItemIndex == widgetPosition) return i + 1
    }

    return size
}

private fun Visitable<*>.isCountedAsProductItem() =
    this is ProductItemVisitable && isCountedAsProductItem()
