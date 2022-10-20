package com.tokopedia.play.ui.view.carousel

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.databinding.ViewProductFeaturedBinding
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.ui.view.carousel.adapter.ProductCarouselAdapter
import com.tokopedia.play.ui.view.carousel.viewholder.ProductCarouselViewHolder
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

    private val impressionSet = mutableSetOf<String>()

    private val adapter = ProductCarouselAdapter(
        listener = object : ProductBasicViewHolder.Listener {
            override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
                listener.onProductClicked(this@ProductCarouselUiView, product, position)
            }
        },
        pinnedProductListener = object : ProductCarouselViewHolder.PinnedProduct.Listener {
            override fun onClicked(
                viewHolder: ProductCarouselViewHolder.PinnedProduct,
                product: PlayProductUiModel.Product
            ) {
                listener.onProductClicked(
                    this@ProductCarouselUiView,
                    product,
                    viewHolder.adapterPosition,
                )
            }

            override fun onAtcClicked(
                viewHolder: ProductCarouselViewHolder.PinnedProduct,
                product: PlayProductUiModel.Product
            ) {
                listener.onAtcClicked(this@ProductCarouselUiView, product)
            }

            override fun onBuyClicked(
                viewHolder: ProductCarouselViewHolder.PinnedProduct,
                product: PlayProductUiModel.Product
            ) {
                listener.onBuyClicked(this@ProductCarouselUiView, product)
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
            sendImpression()
        }
    }

    private val defaultItemDecoration = ProductFeaturedItemDecoration(context)

    init {
        binding.rvProductFeatured.itemAnimator = null
        binding.rvProductFeatured.layoutManager = layoutManager
        binding.rvProductFeatured.adapter = adapter

        binding.rvProductFeatured.addItemDecoration(defaultItemDecoration)
        binding.rvProductFeatured.addOnScrollListener(scrollListener)
    }

    fun setProducts(
        products: List<PlayProductUiModel.Product>,
    ) {
        if (products == adapter.getItems()) return

        invalidateItemDecorations()
        impressionSet.clear()

        adapter.setItemsAndAnimateChanges(products)
        sendImpression()
    }

    fun scrollToFirstPosition() {
        binding.rvProductFeatured.scrollToPosition(0)
    }

    fun setLoading() {
        val placeholders = getPlaceholder()
        if (placeholders != adapter.getItems()) invalidateItemDecorations()

        adapter.setItemsAndAnimateChanges(getPlaceholder())
    }

    fun setFadingEndBounds(width: Int) {
        binding.rvProductFeatured.removeItemDecoration(defaultItemDecoration)
        binding.rvProductFeatured.addItemDecoration(
            ProductFeaturedItemDecoration(binding.rvProductFeatured.context, extraEndMargin = width)
        )
        binding.rvProductFeatured.setFadingEndBounds(width)
    }

    fun setTransparent(isTransparent: Boolean) {
        binding.root.alpha = if (isTransparent) 0f else 1f
    }

    fun show() {
        binding.root.visible()
    }

    fun hide() {
        binding.root.gone()
    }

    fun cleanUp() {
        binding.rvProductFeatured.removeOnScrollListener(scrollListener)
    }

    private fun getPlaceholder() = List(3) { PlayProductUiModel.Placeholder }

    private fun invalidateItemDecorations() {
        try {
            binding.rvProductFeatured.post {
                binding.rvProductFeatured.invalidateItemDecorations()
            }
        } catch (ignored: IllegalStateException) {}
    }

    private fun sendImpression() = synchronized(impressionSet) {
        val products = getVisibleProducts()
        val productsToBeImpressed = products.filter {
            !impressionSet.contains(it.key.id)
        }
        listener.onProductImpressed(this, productsToBeImpressed)
        productsToBeImpressed.forEach {
            impressionSet.add(it.key.id)
        }
    }

    /**
     * Analytic Helper
     */
    private fun getVisibleProducts(): Map<PlayProductUiModel.Product, Int> {
        val products = adapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = layoutManager.findFirstVisibleItemPosition()
            val endPosition = layoutManager.findLastVisibleItemPosition()
            if (startPosition > -1 && endPosition < products.size) {
                return (startPosition..endPosition)
                    .filter { products[it] is PlayProductUiModel.Product }
                    .associateBy { products[it] as PlayProductUiModel.Product }
            }
        }
        return emptyMap()
    }

    interface Listener {

        fun onProductImpressed(view: ProductCarouselUiView, productMap: Map<PlayProductUiModel.Product, Int>)
        fun onProductClicked(view: ProductCarouselUiView, product: PlayProductUiModel.Product, position: Int)

        fun onAtcClicked(view: ProductCarouselUiView, product: PlayProductUiModel.Product)
        fun onBuyClicked(view: ProductCarouselUiView, product: PlayProductUiModel.Product)
    }
}