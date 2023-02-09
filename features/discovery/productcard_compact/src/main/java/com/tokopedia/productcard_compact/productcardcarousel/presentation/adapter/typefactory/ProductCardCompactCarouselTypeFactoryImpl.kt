package com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.ProductCardCompactCarouselItemViewHolder
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.ProductCardCompactCarouselSeeMoreViewHolder

class ProductCardCompactCarouselTypeFactoryImpl(
    private val productCardCarouselItemListener: ProductCardCompactCarouselItemViewHolder.TokoNowCarouselProductCardItemListener? = null,
    private val productCardCarouselSeeMoreListener: ProductCardCompactCarouselSeeMoreViewHolder.TokoNowCarouselProductCardSeeMoreListener? = null
): ProductCardCompactCarouselTypeFactory {

    override fun type(
        visitable: Visitable<*>
    ): Int {
        return when (visitable) {
            is ProductCardCompactCarouselItemUiModel -> ProductCardCompactCarouselItemViewHolder.LAYOUT
            is ProductCardCompactCarouselSeeMoreUiModel -> ProductCardCompactCarouselSeeMoreViewHolder.LAYOUT
            else -> {
                throw TypeNotSupportedException.create("Type not supported")
            }
        }
    }

    fun createViewHolder(
        view: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductCardCompactCarouselItemViewHolder.LAYOUT -> ProductCardCompactCarouselItemViewHolder(
                view = view,
                listener = productCardCarouselItemListener
            )
            ProductCardCompactCarouselSeeMoreViewHolder.LAYOUT -> ProductCardCompactCarouselSeeMoreViewHolder(
                view = view,
                listener = productCardCarouselSeeMoreListener
            )
            else -> {
                throw TypeNotSupportedException.create("Layout not supported")
            }
        }
    }
}
