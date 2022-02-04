package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.itemdecoration.ProductListItemDecoration
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.util.scroll.EndlessRecyclerViewScrollListener
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
internal class ProductListViewComponent(
    private val view: RecyclerView,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    private val adapter = ProductListAdapter(
        onSelected = { eventBus.emit(Event.OnSelected(it)) },
        onLoading = { eventBus.emit(Event.OnLoadMore) }
    )

    init {
        view.adapter = adapter
        view.layoutManager = StaggeredGridLayoutManager(
            2,
            RecyclerView.VERTICAL,
        )
        view.addItemDecoration(ProductListItemDecoration(view.context))
    }

    fun setProductList(
        productList: List<ProductUiModel>,
        selectedList: List<ProductUiModel>,
        showLoading: Boolean,
    ) {
        adapter.setItemsAndAnimateChanges(
            productList.map { product ->
                ProductListAdapter.Model.Product(
                    product = product,
                    isSelected = selectedList.any { it.id == product.id }
                )
            } + if (showLoading) listOf(ProductListAdapter.Model.Loading) else emptyList()
        )

        view.invalidateItemDecorations()
    }

    sealed class Event {
        data class OnSelected(val product: ProductUiModel) : Event()
        object OnLoadMore : Event()
    }
}