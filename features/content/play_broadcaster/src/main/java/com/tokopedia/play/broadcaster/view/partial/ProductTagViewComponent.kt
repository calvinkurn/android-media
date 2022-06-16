package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.ProductTagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.view.adapter.PlayProductTagAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class ProductTagViewComponent(
    container: ViewGroup,
    private val listener: Listener,
) : ViewComponent(container, R.id.layout_product_tag) {

    private val rvProductTag: RecyclerView = findViewById(R.id.rv_bro_product_tag)

    private val adapter = PlayProductTagAdapter()

    private val layoutManager = object : LinearLayoutManager(rvProductTag.context, RecyclerView.HORIZONTAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            onScrollProduct()
        }
    }

    private val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) onScrollProduct()
        }
    }

    private var isProductInitialized = false

    init {
        rvProductTag.layoutManager = layoutManager
        rvProductTag.adapter = adapter
        rvProductTag.addItemDecoration(ProductTagItemDecoration(rvProductTag.context))

        setLoading()
    }

    private fun getVisibleProduct(): Pair<ProductUiModel, Int>? {
        val products = adapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val endPosition = layoutManager.findLastVisibleItemPosition()
            if (startPosition > -1 && endPosition < products.size) return products.slice(startPosition..endPosition)
                .filterIsInstance<ProductUiModel>()
                .mapIndexed { index, item ->
                    Pair(item, startPosition + index)
                }
                .last()
        }
        return null
    }

    private fun onScrollProduct() {
        getVisibleProduct()?.let {
            listener.scrollProductTag(this, it.first, it.second)
        }
    }

    fun setProducts(products: List<ProductUiModel>) {
        if(!isProductInitialized && products.isNotEmpty()) {
            listener.impressProductTag(this)
            isProductInitialized = true
        }

        adapter.setItemsAndAnimateChanges(products)
        rvProductTag.invalidateItemDecorations()
    }

    private fun setLoading() {
        adapter.setItemsAndAnimateChanges(List(MAX_PLACEHOLDER) { })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvProductTag.removeOnScrollListener(scrollListener)
    }

    companion object {
        private const val MAX_PLACEHOLDER = 5
    }

    interface Listener {
        fun impressProductTag(view: ProductTagViewComponent)
        fun scrollProductTag(view: ProductTagViewComponent, product: ProductUiModel, position: Int)
    }
}