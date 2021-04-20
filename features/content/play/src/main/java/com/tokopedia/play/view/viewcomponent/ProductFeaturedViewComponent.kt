package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productfeatured.itemdecoration.ProductFeaturedItemDecoration
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedSeeMoreViewHolder
import com.tokopedia.play.ui.productfeatured.viewholder.ProductFeaturedViewHolder
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

    private val adapter = ProductFeaturedAdapter(
            productFeaturedListener = object : ProductFeaturedViewHolder.Listener {
                override fun onProductCardImpressed(product: PlayProductUiModel.Product, position: Int) {
                    listener.onProductFeaturedImpressed(this@ProductFeaturedViewComponent, product, position)
                }

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

    init {
        rvProductFeatured.adapter = adapter
        rvProductFeatured.addItemDecoration(ProductFeaturedItemDecoration(rvProductFeatured.context))
    }

    fun setFeaturedProducts(products: List<PlayProductUiModel>, maxProducts: Int) {
        val featuredItems = getFinalFeaturedItems(products, maxProducts)
        adapter.setItemsAndAnimateChanges(featuredItems)

        if (featuredItems.isEmpty()) hide()
        else show()
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

    companion object {

        private const val TOTAL_PLACEHOLDER = 3
    }

    interface Listener {

        fun onProductFeaturedImpressed(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int)
        fun onProductFeaturedClicked(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int)
        fun onSeeMoreClicked(view: ProductFeaturedViewComponent)
    }
}