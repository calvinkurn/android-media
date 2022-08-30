package com.tokopedia.content.common.producttag.util.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.content.common.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.content.common.producttag.view.adapter.ShopCardAdapter
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.max

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
val Set<ProductTagSource>.currentSource: ProductTagSource
    get() = lastOrNull() ?: ProductTagSource.Unknown

fun Set<ProductTagSource>.removeLast(): Set<ProductTagSource> {
    return toMutableSet().apply {
        lastOrNull()?.let { remove(it) }
    }
}

internal val Throwable.isNetworkError: Boolean
    get() = this is ConnectException ||
            this is SocketTimeoutException ||
            this is UnknownHostException


fun StaggeredGridLayoutManager.getVisibleItems(adapter: ProductTagCardAdapter): List<Pair<ProductUiModel, Int>> {
    val items = adapter.getItems()
    val (start, end) = getVisibleItemsPosition(this, adapter)

    if (start > -1 && end < items.size && start <= end) {
        return items.slice(start..end)
            .filterIsInstance<ProductTagCardAdapter.Model.Product>()
            .mapIndexed { index, item ->
                Pair(item.product, start + index)
            }
    }

    return emptyList()
}

fun StaggeredGridLayoutManager.getVisibleItems(adapter: MyShopProductAdapter): List<Pair<ProductUiModel, Int>> {
    val items = adapter.getItems()
    val (start, end) = getVisibleItemsPosition(this, adapter)

    if (start > -1 && end < items.size && start <= end) {
        return items.slice(start..end)
            .filterIsInstance<MyShopProductAdapter.Model.Product>()
            .mapIndexed { index, item ->
                Pair(item.product, start + index)
            }
    }

    return emptyList()
}

fun LinearLayoutManager.getVisibleItems(adapter: ShopCardAdapter): List<Pair<ShopUiModel, Int>> {
    val items = adapter.getItems()
    val (start, end) = getVisibleItemsPosition(this, adapter)

    if (start > -1 && end < items.size && start <= end) {
        return items.slice(start..end)
            .filterIsInstance<ShopCardAdapter.Model.Shop>()
            .mapIndexed { index, item ->
                Pair(item.shop, start + index)
            }
    }

    return emptyList()
}

private fun <T: Any> getVisibleItemsPosition(
    layoutManager: StaggeredGridLayoutManager,
    adapter: BaseDiffUtilAdapter<T>,
): Pair<Int, Int> {

    val products = adapter.getItems()
    if (products.isNotEmpty()) {
        val startPositionArray = layoutManager.findFirstCompletelyVisibleItemPositions(null)
        val endPositionArray = layoutManager.findLastVisibleItemPositions(null)

        val startPosition = startPositionArray[0]
        val endPosition = max(endPositionArray[0], endPositionArray[1])

        return Pair(startPosition, endPosition)
    }

    return Pair(-1, -1)
}

private fun <T: Any> getVisibleItemsPosition(
    layoutManager: LinearLayoutManager,
    adapter: BaseDiffUtilAdapter<T>,
): Pair<Int, Int> {

    val products = adapter.getItems()
    if (products.isNotEmpty()) {
        val startPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val endPosition = layoutManager.findLastVisibleItemPosition()

        return Pair(startPosition, endPosition)
    }

    return Pair(-1, -1)
}

fun List<SelectedProductUiModel>.isProductFound(product: ProductUiModel): Boolean {
    return find { it.id == product.id } != null
}