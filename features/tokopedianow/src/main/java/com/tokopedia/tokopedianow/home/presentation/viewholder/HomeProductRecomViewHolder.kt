package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel

class HomeProductRecomViewHolder (
    itemView: View,
): AbstractViewHolder<HomeProductRecomUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private val productCard: CarouselProductCardView by lazy { itemView.findViewById(R.id.product_card) }

    override fun bind(element: HomeProductRecomUiModel?) {
        val el = element
        val list = mutableListOf<ProductCardModel>()
        element?.list?.forEach {
            list.add(it.productCardModel)
        }

        productCard.bindCarouselProductCardViewGrid(
            list,
            showSeeMoreCard = true
        )
    }
}