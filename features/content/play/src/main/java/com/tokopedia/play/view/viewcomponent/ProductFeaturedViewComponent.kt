package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.view.custom.ProductFeaturedRecyclerView
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.view_product_featured) {

    private val rvProductFeatured: ProductFeaturedRecyclerView = findViewById(R.id.rv_product_featured)

    private val adapter = ProductFeaturedAdapter(
        productFeaturedListener = object : ProductBasicViewHolder.Listener {
            override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
                listener.onProductFeaturedClicked(this@ProductFeaturedViewComponent, product, position)
            }
        }
    )

    private val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) sendImpression()
        }
    }

    private val layoutManager = object : LinearLayoutManager(rvProductFeatured.context, RecyclerView.HORIZONTAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            listener.onProductFeaturedImpressed(this@ProductFeaturedViewComponent, getVisibleProducts())
        }
    }

    private var isProductsInitialized = false

    private val defaultItemDecoration = ProductFeaturedItemDecoration(rvProductFeatured.context)

    init {
        rvProductFeatured.itemAnimator = null
        rvProductFeatured.layoutManager = layoutManager
        rvProductFeatured.adapter = adapter
        rvProductFeatured.addItemDecoration(defaultItemDecoration)
        rvProductFeatured.addOnScrollListener(scrollListener)
    }

    fun setFeaturedProducts(products: List<ProductSectionUiModel>, maxProducts: Int) {
        if (products != adapter.getItems()) invalidateItemDecorations()

        val featuredItems = getFinalFeaturedItems(products, maxProducts)
        adapter.setItemsAndAnimateChanges(featuredItems)

        sendImpression()
    }

    fun showIfNotEmpty() {
        if (adapter.itemCount != 0) show()
        else hide()
    }

    fun setPlaceholder() {
        val placeholders = getPlaceholder()
        if (placeholders != adapter.getItems()) invalidateItemDecorations()

        adapter.setItemsAndAnimateChanges(getPlaceholder())
    }

    fun setFadingEndBounds(width: Int) {
        rvProductFeatured.removeItemDecoration(defaultItemDecoration)
        rvProductFeatured.addItemDecoration(
            ProductFeaturedItemDecoration(rvProductFeatured.context, extraEndMargin = width)
        )
        rvProductFeatured.setFadingEndBounds(width)
    }

    private fun invalidateItemDecorations() {
        try {
            rvProductFeatured.post {
                rvProductFeatured.invalidateItemDecorations()
            }
        } catch (ignored: IllegalStateException) {}
    }

    private fun getFinalFeaturedItems(products: List<ProductSectionUiModel>, maxProducts: Int): List<PlayProductUiModel> {
        return products.flatMap { it.productList }.take(maxProducts) // todo: retest
    }

    private fun getPlaceholder() = List(TOTAL_PLACEHOLDER) { PlayProductUiModel.Placeholder }

    private fun sendImpression() {
        if (isProductsInitialized) {
            listener.onProductFeaturedImpressed(this@ProductFeaturedViewComponent, getVisibleProducts())
        } else isProductsInitialized = true
    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvProductFeatured.removeOnScrollListener(scrollListener)
    }

    /**
     * Analytic Helper
     */
    private fun getVisibleProducts(): List<Pair<PlayProductUiModel.Product, Int>> {
        val products = adapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val endPosition = layoutManager.findLastVisibleItemPosition()
            if (startPosition > -1 && endPosition < products.size) return products.slice(startPosition..endPosition)
                    .filterIsInstance<PlayProductUiModel.Product>()
                    .mapIndexed { index, item ->
                        Pair(item, startPosition + index)
                    }
        }
        return emptyList()
    }

    companion object {
        private const val TOTAL_PLACEHOLDER = 3
    }

    interface Listener {

        fun onProductFeaturedImpressed(view: ProductFeaturedViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>)
        fun onProductFeaturedClicked(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int)
    }
}