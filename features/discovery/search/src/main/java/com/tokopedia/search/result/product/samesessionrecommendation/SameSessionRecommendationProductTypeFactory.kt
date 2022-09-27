package com.tokopedia.search.result.product.samesessionrecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionTypeFactory
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener

class SameSessionRecommendationProductTypeFactory(
    private val listener: InspirationCarouselListener
) : BaseAdapterTypeFactory(), InspirationCarouselOptionTypeFactory {

    override fun type(type: String): Int {
        return when (type) {
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID -> SameSessionRecommendationProductViewHolder.LAYOUT
            else -> SameSessionRecommendationProductViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SameSessionRecommendationProductViewHolder.LAYOUT -> SameSessionRecommendationProductViewHolder(
                parent,
                listener
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
