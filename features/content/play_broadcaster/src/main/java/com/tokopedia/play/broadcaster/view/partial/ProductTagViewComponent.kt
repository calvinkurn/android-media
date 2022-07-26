package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.ProductTagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.carousel.ProductCarouselViewHolder
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

    private val productListener = object : ProductCarouselViewHolder.Product.Listener{
        override fun onPinProductClicked(product: ProductUiModel) {
            listener.onPinProductClicked(product)
        }
    }

    private val pinnedProductListener = object : ProductCarouselViewHolder.PinnedProduct.Listener{
        override fun onPinProductClicked(product: ProductUiModel) {
            listener.onPinProductClicked(product)
        }
    }

    private val adapter = PlayProductTagAdapter(productListener = productListener, pinnedProductListener = pinnedProductListener)

    private val coachMark: CoachMark2 = CoachMark2(container.context)

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

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (itemCount > 0) rvProductTag.smoothScrollToPosition(0)
        }
    }

    private var isProductInitialized = false

    init {
        adapter.registerAdapterDataObserver(adapterObserver)

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
        showCoachMark()
    }

    private fun setLoading() {
        adapter.setItemsAndAnimateChanges(List(MAX_PLACEHOLDER) { })
    }

    private fun showCoachMark() {
        //return if empty
        val holder = rvProductTag.findViewHolderForAdapterPosition(0)
        holder?.let {
            val coachMarkItem = arrayListOf(
                CoachMark2Item(
                    holder.itemView.findViewById(R.id.view_pin_product),
                    "",
                    getString(R.string.play_bro_pinned_coachmark_desc),
                    CoachMark2.POSITION_BOTTOM
                )
            )
         coachMark.showCoachMark(coachMarkItem)
            //delay dismiss
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvProductTag.removeOnScrollListener(scrollListener)
        adapter.unregisterAdapterDataObserver(adapterObserver)
    }

    companion object {
        private const val MAX_PLACEHOLDER = 5
    }

    interface Listener {
        fun impressProductTag(view: ProductTagViewComponent)
        fun scrollProductTag(view: ProductTagViewComponent, product: ProductUiModel, position: Int)
        fun onPinProductClicked(product: ProductUiModel)
    }
}