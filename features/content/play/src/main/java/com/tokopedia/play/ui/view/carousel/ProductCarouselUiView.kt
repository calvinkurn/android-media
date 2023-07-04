package com.tokopedia.play.ui.view.carousel

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.databinding.ViewProductFeaturedBinding
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.ui.view.carousel.adapter.ProductCarouselAdapter
import com.tokopedia.play.ui.view.carousel.viewholder.ProductCarouselViewHolder
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class ProductCarouselUiView(
    private val binding: ViewProductFeaturedBinding,
    private val listener: Listener,
) {

    private val context = binding.root.context

    private val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) sendImpression()
        }
    }

    private val adapter = ProductCarouselAdapter(
        listener = object : ProductBasicViewHolder.Listener {
            override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
                if (position <= lastCompletelyVisible) {
                    listener.onProductClicked(this@ProductCarouselUiView, product, position)
                }
            }
        },
        pinnedProductListener = object : ProductCarouselViewHolder.PinnedProduct.Listener {
            override fun onClicked(
                viewHolder: ProductCarouselViewHolder.PinnedProduct,
                product: PlayProductUiModel.Product,
                position: Int,
            ) {
                if (position <= lastCompletelyVisible) {
                    listener.onProductClicked(
                        this@ProductCarouselUiView,
                        product,
                        viewHolder.adapterPosition,
                    )
                }
            }

            override fun onTransactionClicked(
                viewHolder: ProductCarouselViewHolder.PinnedProduct,
                product: PlayProductUiModel.Product,
                action: ProductAction
            ) {
                listener.onTransactionClicked(this@ProductCarouselUiView, product, action)
            }
        }
    )

    private val layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
            sendImpression()
        }
    }

    private val defaultItemDecoration = ProductFeaturedItemDecoration(context)

    private val lastCompletelyVisible : Int
        get() {
            return layoutManager.findLastCompletelyVisibleItemPosition()
        }

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

        sendImpression()

        adapter.setItemsAndAnimateChanges(products)
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

    val isShown = binding.root.isShown

    fun hide() {
        binding.root.gone()
    }

    private fun getPlaceholder() = List(3) { PlayProductUiModel.Placeholder }

    private fun invalidateItemDecorations() {
        try {
            binding.rvProductFeatured.post {
                binding.rvProductFeatured.invalidateItemDecorations()
            }
        } catch (ignored: IllegalStateException) {}
    }

    /**
     * Expose
     */
    private fun sendImpression()  {
        val products = getVisibleProducts()
        listener.onProductImpressed(this, products)
    }

    /**
     * Analytic Helper
     */
    fun getVisibleProducts(): Map<PlayProductUiModel.Product, Int> {
        val products = adapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val endPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (startPosition > -1 && endPosition < products.size) {
                return (startPosition..endPosition)
                    .filter { products[it] is PlayProductUiModel.Product }
                    .associateBy { products[it] as PlayProductUiModel.Product }
            }
        }
        return emptyMap()
    }

    fun cleanUp() {
        binding.rvProductFeatured.removeOnScrollListener(scrollListener)
    }

    interface Listener {

        fun onProductImpressed(view: ProductCarouselUiView, productMap: Map<PlayProductUiModel.Product, Int>)
        fun onProductClicked(view: ProductCarouselUiView, product: PlayProductUiModel.Product, position: Int)

        fun onTransactionClicked(view: ProductCarouselUiView, product: PlayProductUiModel.Product, action: ProductAction)
    }
}
