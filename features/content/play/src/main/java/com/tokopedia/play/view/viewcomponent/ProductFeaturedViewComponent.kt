package com.tokopedia.play.view.viewcomponent

import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedSeeMoreViewHolder
import com.tokopedia.play.view.custom.ProductIconView
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewComponent(
        container: ViewGroup,
        private val listener: Listener
) : ViewComponent(container, R.id.view_product_featured) {

    private val rvProductFeatured: RecyclerView = findViewById(R.id.rv_product_featured)
    private val icProductSeeMore: ProductIconView = findViewById(R.id.ic_product_seemore)

    private val adapter = ProductFeaturedAdapter(
            productFeaturedListener = object : ProductBasicViewHolder.Listener {
                override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {
                    listener.onProductFeaturedClicked(this@ProductFeaturedViewComponent, product, position)
                }
            },
            productSeeMoreListener = object : ProductFeaturedSeeMoreViewHolder.Listener {
                override fun onSeeMoreClicked() {
                    listener.onSeeMoreClicked(this@ProductFeaturedViewComponent)
                }
            }
    )

    private val scrollListener = object: RecyclerView.OnScrollListener(){
        val bias = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9F, resources.displayMetrics)

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val offset = rvProductFeatured.computeHorizontalScrollOffset()
            val width = rvProductFeatured.width
            val range = rvProductFeatured.computeHorizontalScrollRange()
            val percentage = (offset + width) / range.toFloat()

            Log.d("<LOG>", "=======================")
            Log.d("<LOG>", "vertical offset : $offset")
            Log.d("<LOG>", "vertical width : $width")
            Log.d("<LOG>", "vertical range : $range")
            Log.d("<LOG>", "percentage : $percentage")

            if(width > range) icProductSeeMore.gone()
            else if(percentage > 0.9F) {
                val scale = (1 - percentage) / 0.1F
                val reverseScale = if(1 - scale < 0) 0F else 1 - scale

                val productFeatured = adapter.getItems()
                if(!productFeatured.isNullOrEmpty() && productFeatured.first() is PlayProductUiModel.Product) {
                    val seeMore = productFeatured.last() as PlayProductUiModel.SeeMore
                    productFeatured.toMutableList()[productFeatured.size - 1] = seeMore.copy(scale = reverseScale)
//                    adapter.setItemsAndAnimateChanges(productFeatured)

                    if(reverseScale < 0.73F) icProductSeeMore.visible() else icProductSeeMore.gone()
                }
            }
            else icProductSeeMore.visible()
        }

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

    init {
        icProductSeeMore.setBackgroundResource(R.drawable.ic_product_see_more)
        rvProductFeatured.layoutManager = layoutManager
        rvProductFeatured.adapter = adapter
        rvProductFeatured.addItemDecoration(ProductFeaturedItemDecoration(rvProductFeatured.context))
        rvProductFeatured.addOnScrollListener(scrollListener)
    }

    fun setFeaturedProducts(products: List<PlayProductUiModel>, maxProducts: Int) {
        val featuredItems = getFinalFeaturedItems(products, maxProducts)
        adapter.setItemsAndAnimateChanges(featuredItems)

        if (featuredItems.isEmpty()) hide()
        else {
            show()
            if(featuredItems.first() is PlayProductUiModel.Product)
                icProductSeeMore.setTotalProduct(products.size)
        }

        sendImpression()
    }

    fun showIfNotEmpty() {
        if (adapter.itemCount != 0 &&
                adapter.getItem(0) !is PlayProductUiModel.Placeholder) show()
        else hide()
    }

    fun showPlaceholder() {
        setFeaturedProducts(getPlaceholder(), TOTAL_PLACEHOLDER)
    }

    private fun getFinalFeaturedItems(products: List<PlayProductUiModel>, maxProducts: Int): List<PlayProductUiModel> {
        val featuredProducts = products.take(maxProducts)
        return if (featuredProducts.isNotEmpty()) {
            if (featuredProducts.first() is PlayProductUiModel.Product && featuredProducts.last() !is PlayProductUiModel.SeeMore) featuredProducts + PlayProductUiModel.SeeMore(products.size, 1F)
            else featuredProducts
        }
        else featuredProducts
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
        fun onSeeMoreClicked(view: ProductFeaturedViewComponent)
    }
}