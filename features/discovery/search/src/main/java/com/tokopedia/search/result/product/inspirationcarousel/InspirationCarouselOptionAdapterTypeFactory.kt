package com.tokopedia.search.result.product.inspirationcarousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class InspirationCarouselOptionAdapterTypeFactory(
        private val listener: InspirationCarouselListener
) : BaseAdapterTypeFactory(), InspirationCarouselOptionTypeFactory {

    override fun type(type: String): Int {
        return when(type) {
            LAYOUT_INSPIRATION_CAROUSEL_INFO -> InspirationCarouselOptionInfoViewHolder.LAYOUT
            LAYOUT_INSPIRATION_CAROUSEL_GRID -> InspirationCarouselOptionGridViewHolder.LAYOUT
            LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER -> InspirationCarouselOptionGridBannerViewHolder.LAYOUT
            else -> InspirationCarouselOptionListViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InspirationCarouselOptionListViewHolder.LAYOUT ->
                InspirationCarouselOptionListViewHolder(parent, listener)
            InspirationCarouselOptionInfoViewHolder.LAYOUT ->
                InspirationCarouselOptionInfoViewHolder(parent, listener)
            InspirationCarouselOptionGridViewHolder.LAYOUT ->
                InspirationCarouselOptionGridViewHolder(parent, listener)
            InspirationCarouselOptionGridBannerViewHolder.LAYOUT ->
                InspirationCarouselOptionGridBannerViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
