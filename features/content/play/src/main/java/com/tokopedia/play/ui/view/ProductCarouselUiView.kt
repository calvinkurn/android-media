package com.tokopedia.play.ui.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.databinding.ViewProductFeaturedBinding
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class ProductCarouselUiView(
    private val binding: ViewProductFeaturedBinding,
    private val listener: Listener,
) {

    private val context = binding.root.context

    private val adapter = ProductFeaturedAdapter(
        productFeaturedListener = object : ProductBasicViewHolder.Listener {
            override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
                listener.onProductClicked(this@ProductCarouselUiView, product, position)
            }
        }
    )

    private val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) sendImpression()
        }
    }

    private val layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            listener.onProductImpressed(this@ProductCarouselUiView, getVisibleProducts())
        }
    }

    private val defaultItemDecoration = ProductFeaturedItemDecoration(context)

    private var isProductsInitialized = false

    init {
        binding.rvProductFeatured.apply {
            itemAnimator = null
            layoutManager = layoutManager
            adapter = this@ProductCarouselUiView.adapter
        }.run {
            addItemDecoration(defaultItemDecoration)
            addOnScrollListener(scrollListener)
        }
    }

    fun setProducts(products: List<ProductSectionUiModel>, maxProducts: Int) {
        if (products != adapter.getItems()) invalidateItemDecorations()

        val featuredItems = getFinalFeaturedItems(products, maxProducts)
        adapter.setItemsAndAnimateChanges(featuredItems)

        sendImpression()
    }

    fun setLoading() {
        val placeholders = getPlaceholder()
        if (placeholders != adapter.getItems()) invalidateItemDecorations()

        adapter.setItemsAndAnimateChanges(getPlaceholder())
    }

    fun show() {
        binding.root.visible()
    }

    fun hide() {
        binding.root.gone()
    }

    private fun getPlaceholder() = List(3) { PlayProductUiModel.Placeholder }

    private fun getFinalFeaturedItems(products: List<ProductSectionUiModel>, maxProducts: Int): List<PlayProductUiModel> {
        return products
            .filterIsInstance<ProductSectionUiModel.Section>()
            .flatMap { it.productList }
            .take(maxProducts)
    }

    private fun invalidateItemDecorations() {
        try {
            binding.rvProductFeatured.post {
                binding.rvProductFeatured.invalidateItemDecorations()
            }
        } catch (ignored: IllegalStateException) {}
    }

    private fun sendImpression() {
        if (isProductsInitialized) {
            listener.onProductImpressed(this@ProductCarouselUiView, getVisibleProducts())
        } else isProductsInitialized = true
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

    interface Listener {

        fun onProductImpressed(view: ProductCarouselUiView, products: List<Pair<PlayProductUiModel.Product, Int>>)
        fun onProductClicked(view: ProductCarouselUiView, product: PlayProductUiModel.Product, position: Int)
    }
}