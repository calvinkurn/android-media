package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.itemdecoration.ProductListItemDecoration
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
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
    ) {
        adapter.setItemsAndAnimateChanges(
            productList.map { product ->
                ProductListAdapter.Model(
                    product = product,
                    isSelected = selectedList.any { it.id == product.id }
                )
            }
        )
    }

    sealed class Event {
        data class OnSelected(val product: ProductUiModel) : Event()
    }
}