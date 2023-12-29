package com.tokopedia.search.result.product.inspirationcarousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.reimagine.Search2Component

class InspirationCarouselOptionAdapterTypeFactory(
    private val listener: InspirationCarouselListener,
    private val reimagineSearch2Component: Search2Component,
) : BaseAdapterTypeFactory(), InspirationCarouselOptionTypeFactory {

    private val isReimagine: Boolean
        get() = reimagineSearch2Component.isReimagineCarousel()

    override fun type(type: String): Int {
        return when(type) {
            LAYOUT_INSPIRATION_CAROUSEL_INFO ->
                InspirationCarouselOptionInfoViewHolder.LAYOUT
            LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                inspirationCarouselGridLayoutType()
            LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER ->
                InspirationCarouselOptionGridBannerViewHolder.LAYOUT
            else ->
                InspirationCarouselOptionListViewHolder.LAYOUT
        }
    }

    private fun inspirationCarouselGridLayoutType() =
        if (isReimagine) InspirationCarouselOptionGridReimagineViewHolder.LAYOUT
        else InspirationCarouselOptionGridViewHolder.LAYOUT

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
            InspirationCarouselOptionGridReimagineViewHolder.LAYOUT ->
                InspirationCarouselOptionGridReimagineViewHolder(
                    parent,
                    listener,
                    reimagineSearch2Component.hasMultilineProductName(),
                )
            else -> super.createViewHolder(parent, type)
        }
    }
}
