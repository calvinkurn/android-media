package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedSeeMoreViewHolder
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

    private val availableFeaturedProducts: MutableMap<String, PlayProductUiModel> = mutableMapOf()

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
    private val productFeaturedListener = object: RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                sendImpression()
            }
        }
    }

    init {
        rvProductFeatured.adapter = adapter
        rvProductFeatured.addItemDecoration(ProductFeaturedItemDecoration(rvProductFeatured.context))
        rvProductFeatured.addOnScrollListener(productFeaturedListener)
    }

    fun setFeaturedProducts(products: List<PlayProductUiModel>, maxProducts: Int) {
        val featuredItems = getFinalFeaturedItems(products, maxProducts)
        adapter.setItemsAndAnimateChanges(featuredItems)

        availableFeaturedProducts.clear()
        featuredItems.forEach {
            if (it is PlayProductUiModel.Product) availableFeaturedProducts[it.id] = it
        }

        if (featuredItems.isEmpty()) hide()
        else {
            show()
            rvProductFeatured.addOneTimeGlobalLayoutListener { sendImpression() }
        }
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
            if (featuredProducts.first() is PlayProductUiModel.Product && featuredProducts.last() != PlayProductUiModel.SeeMore) featuredProducts + PlayProductUiModel.SeeMore
            else featuredProducts
        }
        else featuredProducts
    }

    private fun getPlaceholder() = List(TOTAL_PLACEHOLDER) { PlayProductUiModel.Placeholder }

    private fun sendImpression() {
        val layoutManager = rvProductFeatured.layoutManager
        if (layoutManager !is LinearLayoutManager) return

        val startPosition = layoutManager.findFirstVisibleItemPosition()
        val endPosition = layoutManager.findLastVisibleItemPosition()
        if (startPosition < 0 || endPosition > adapter.itemCount) return

        val productImpressed = getProductImpressed(startPosition, endPosition)
        if (productImpressed.isNotEmpty()) listener.onProductFeaturedImpressed(this@ProductFeaturedViewComponent, productImpressed)
    }

    private fun getProductImpressed(startPosition: Int, endPosition: Int): List<PlayProductUiModel.Product> {
        val productImpressed = adapter.getItems()
                .slice(startPosition..endPosition)
                .filterIsInstance<PlayProductUiModel.Product>()
                .filter { availableFeaturedProducts.containsKey(it.id) }

        val keySet = productImpressed.map { it.id }.toSet()
        availableFeaturedProducts.keys.removeAll(keySet)
        return productImpressed
    }

    companion object {

        private const val TOTAL_PLACEHOLDER = 3
    }

    interface Listener {

        fun onProductFeaturedImpressed(view: ProductFeaturedViewComponent, products: List<PlayProductUiModel.Product>)
        fun onProductFeaturedClicked(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int)
        fun onSeeMoreClicked(view: ProductFeaturedViewComponent)
    }
}