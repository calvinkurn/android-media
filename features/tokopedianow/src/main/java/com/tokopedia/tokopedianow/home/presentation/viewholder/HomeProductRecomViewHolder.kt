package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel

class HomeProductRecomViewHolder (
    itemView: View,
    private val listener: HomeProductRecomListener? = null
): AbstractViewHolder<HomeProductRecomUiModel>(itemView), CarouselProductCardListener.OnItemClickListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private val productCardWidget: CarouselProductCardView by lazy { itemView.findViewById(R.id.product_card) }

    private var productCards: List<HomeProductCardUiModel> = listOf()

    override fun bind(element: HomeProductRecomUiModel) {
        productCards = element.productCards
        productCardWidget.bindCarouselProductCardViewGrid(
            mapToProductCard(element),
            carouselProductCardOnItemClickListener = this,
            recyclerViewPool = listener?.getCarouselRecycledViewPool()
        )
    }

    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productCards[carouselProductCardPosition].id
        )
    }

    private fun mapToProductCard(element: HomeProductRecomUiModel): List<ProductCardModel> {
        val productCards = mutableListOf<ProductCardModel>()
        element.productCards.map {
            productCards.add(it.productCardModel)
        }
        return productCards
    }

    interface HomeProductRecomListener {
        fun getCarouselRecycledViewPool(): RecyclerView.RecycledViewPool
    }
}