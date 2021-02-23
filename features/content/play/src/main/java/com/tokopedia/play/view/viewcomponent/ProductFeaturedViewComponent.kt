package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.ui.productfeatured.adapter.ProductFeaturedAdapter
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewComponent(
        container: ViewGroup,
) : ViewComponent(container, R.id.rv_product_featured) {

    private val rvProductFeatured = rootView as RecyclerView

    private val adapter = ProductFeaturedAdapter(object : ProductBasicViewHolder.Listener {
        override fun onClickProductCard(product: PlayProductUiModel.Product, position: Int) {

        }
    })

    init {
        rvProductFeatured.adapter = adapter
    }

    fun setFeaturedProducts(featuredProducts: List<PlayProductUiModel>) {
        adapter.setItemsAndAnimateChanges(featuredProducts)
    }
}