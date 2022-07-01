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

    private fun getScrollListener(section: ProductSectionUiModel.Section) = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) sendImpression(section)
        }
    }

    private fun getLayoutManager(section: ProductSectionUiModel.Section) = object : LinearLayoutManager(rvProductFeatured.context, RecyclerView.HORIZONTAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            listener.onProductFeaturedImpressed(this@ProductFeaturedViewComponent, getVisibleProducts(section), section)
        }
    }

    private var isProductsInitialized = false

    private val defaultItemDecoration = ProductFeaturedItemDecoration(rvProductFeatured.context)

    init {
        rvProductFeatured.itemAnimator = null
        rvProductFeatured.adapter = adapter
        rvProductFeatured.addItemDecoration(defaultItemDecoration)
    }

    fun setFeaturedProducts(products: List<ProductSectionUiModel>, maxProducts: Int) {
        if (products != adapter.getItems()) invalidateItemDecorations()
        val section = products.filterIsInstance<ProductSectionUiModel.Section>().first()

        rvProductFeatured.layoutManager = getLayoutManager(section)
        rvProductFeatured.addOnScrollListener(getScrollListener(section))

        val featuredItems = getFinalFeaturedItems(products, maxProducts)
        adapter.setItemsAndAnimateChanges(featuredItems)

        sendImpression(section)
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

    fun setTransparent(isTransparent: Boolean) {
        rootView.alpha = if (isTransparent) 0f else 1f
    }

    private fun invalidateItemDecorations() {
        try {
            rvProductFeatured.post {
                rvProductFeatured.invalidateItemDecorations()
            }
        } catch (ignored: IllegalStateException) {}
    }

    private fun getFinalFeaturedItems(products: List<ProductSectionUiModel>, maxProducts: Int): List<PlayProductUiModel> {
        return products
            .filterIsInstance<ProductSectionUiModel.Section>()
            .flatMap { it.productList }
            .take(maxProducts)
    }

    private fun getPlaceholder() = List(TOTAL_PLACEHOLDER) { PlayProductUiModel.Placeholder }

    private fun sendImpression(section: ProductSectionUiModel.Section) {
        if (isProductsInitialized) {
            listener.onProductFeaturedImpressed(this@ProductFeaturedViewComponent, getVisibleProducts(section), section)
        } else isProductsInitialized = true
    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvProductFeatured.removeOnScrollListener(getScrollListener(section = ProductSectionUiModel.Section.Empty))
    }

    /**
     * Analytic Helper
     */
    private fun getVisibleProducts(section: ProductSectionUiModel.Section): List<Pair<PlayProductUiModel.Product, Int>> {
        val products = adapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = getLayoutManager(section).findFirstCompletelyVisibleItemPosition()
            val endPosition = getLayoutManager(section).findLastVisibleItemPosition()
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

        fun onProductFeaturedImpressed(view: ProductFeaturedViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>, section: ProductSectionUiModel.Section)
        fun onProductFeaturedClicked(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int)
    }
}