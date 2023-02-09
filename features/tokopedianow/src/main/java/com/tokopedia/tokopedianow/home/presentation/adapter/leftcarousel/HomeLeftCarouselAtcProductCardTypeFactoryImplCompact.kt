package com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory.ProductCardCompactCarouselTypeFactory
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.ProductCardCompactCarouselSeeMoreViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardViewHolder

class HomeLeftCarouselAtcProductCardTypeFactoryImplCompact(
    private val productCardListener: HomeLeftCarouselAtcProductCardViewHolder.HomeLeftCarouselAtcProductCardListener? = null,
    private val productCardSeeMoreListener: ProductCardCompactCarouselSeeMoreViewHolder.TokoNowCarouselProductCardSeeMoreListener? = null
): HomeLeftCarouselAtcProductCardTypeFactory, ProductCardCompactCarouselTypeFactory {

    override fun type(visitable: Visitable<*>): Int {
        return when (visitable) {
            is HomeLeftCarouselAtcProductCardUiModel -> HomeLeftCarouselAtcProductCardViewHolder.LAYOUT
            is ProductCardCompactCarouselSeeMoreUiModel -> ProductCardCompactCarouselSeeMoreViewHolder.LAYOUT
            else -> {
                throw TypeNotSupportedException.create("Type not supported")
            }
        }
    }

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLeftCarouselAtcProductCardViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardViewHolder(view, productCardListener)
            ProductCardCompactCarouselSeeMoreViewHolder.LAYOUT -> ProductCardCompactCarouselSeeMoreViewHolder(view, productCardSeeMoreListener)
            else -> {
                throw TypeNotSupportedException.create("Layout not supported")
            }
        }
    }
}
