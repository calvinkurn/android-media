package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.itemdecoration.ProductListItemDecoration
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
internal class ProductListViewComponent(
    view: RecyclerView,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    private val adapter = ProductListAdapter {
        eventBus.emit(Event.OnSelected(it))
    }

    private val scrollListener: EndlessRecyclerViewScrollListener

    init {
        view.adapter = adapter
        view.layoutManager = StaggeredGridLayoutManager(
            2,
            RecyclerView.VERTICAL,
        )
        scrollListener = object : EndlessRecyclerViewScrollListener(view.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                eventBus.emit(Event.OnLoadMore(page))
            }
        }
        view.addOnScrollListener(scrollListener)
        view.addItemDecoration(ProductListItemDecoration(view.context))
    }

    fun setProductList(
        productList: List<ProductUiModel>,
        selectedList: List<ProductUiModel>,
        isSuccess: Boolean,
        hasNextPage: Boolean,
    ) {
        adapter.setItemsAndAnimateChanges(
            productList.map { product ->
                ProductListAdapter.Model(
                    product = product,
                    isSelected = selectedList.any { it.id == product.id }
                )
            }
        )
        scrollListener.updateState(isSuccess = isSuccess)
        scrollListener.setHasNextPage(hasNextPage)
    }

    fun loadNextPage() {
        scrollListener.loadMoreNextPage()
    }

    sealed class Event {
        data class OnSelected(val product: ProductUiModel) : Event()
        data class OnLoadMore(val page: Int) : Event()
    }
}