package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        listener: Listener
) : ViewComponent(container, R.id.view_product_featured) {

    private val rvProductFeatured: RecyclerView = findViewById(R.id.rv_product_featured)

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

    init {
        rvProductFeatured.adapter = adapter
        rvProductFeatured.addItemDecoration(ProductFeaturedItemDecoration(rvProductFeatured.context))
    }

    fun setFeaturedProducts(featuredProducts: List<PlayProductUiModel>) {
        adapter.setItemsAndAnimateChanges(getFinalFeaturedItems(featuredProducts))
    }

    private fun getFinalFeaturedItems(featuredProducts: List<PlayProductUiModel>): List<PlayProductUiModel> {
        return if (featuredProducts.isNotEmpty() && featuredProducts.last() != PlayProductUiModel.SeeMore) featuredProducts + PlayProductUiModel.SeeMore
        else featuredProducts
    }

    interface Listener {

        fun onProductFeaturedClicked(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int)
        fun onSeeMoreClicked(view: ProductFeaturedViewComponent)
    }
}