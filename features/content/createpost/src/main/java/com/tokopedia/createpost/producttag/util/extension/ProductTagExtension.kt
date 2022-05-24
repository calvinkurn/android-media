package com.tokopedia.createpost.producttag.util.extension

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.createpost.producttag.view.adapter.MyShopProductAdapter
import com.tokopedia.createpost.producttag.view.adapter.ProductTagCardAdapter
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.max
import kotlin.reflect.KClass

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
    return getVisibleItems<ProductTagCardAdapter.Model, ProductTagCardAdapter.Model.Product>(this, adapter)
}

fun StaggeredGridLayoutManager.getVisibleItems(adapter: MyShopProductAdapter): List<Pair<ProductUiModel, Int>> {
    return getVisibleItems<MyShopProductAdapter.Model, MyShopProductAdapter.Model.Product>(this, adapter)
}

private inline fun <T: Any, reified R> getVisibleItems(
    layoutManager: StaggeredGridLayoutManager,
    adapter: BaseDiffUtilAdapter<T>,
): List<Pair<ProductUiModel, Int>> {

    val products = adapter.getItems()
    if (products.isNotEmpty()) {
        val startPositionArray = layoutManager.findFirstCompletelyVisibleItemPositions(null)
        val endPositionArray = layoutManager.findLastVisibleItemPositions(null)

        val startPosition = startPositionArray[0]
        val endPosition = max(endPositionArray[0], endPositionArray[1])

        if (startPosition > -1 && endPosition < products.size) {
            return products.slice(startPosition..endPosition)
                .filterIsInstance<R>()
                .filterIsInstance<MyShopProductAdapter.Model.Product>()
                .mapIndexed { index, item ->
                    Pair(item.product, startPosition + index)
                }
        }
    }

    return emptyList()
}