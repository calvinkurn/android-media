package com.tokopedia.search.result.presentation.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.*
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselOptionGridBannerViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselOptionGridViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselOptionInfoViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselOptionListViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener

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
            InspirationCarouselOptionListViewHolder.LAYOUT -> InspirationCarouselOptionListViewHolder(parent, listener)
            InspirationCarouselOptionInfoViewHolder.LAYOUT -> InspirationCarouselOptionInfoViewHolder(parent, listener)
            InspirationCarouselOptionGridViewHolder.LAYOUT -> InspirationCarouselOptionGridViewHolder(parent, listener)
            InspirationCarouselOptionGridBannerViewHolder.LAYOUT -> InspirationCarouselOptionGridBannerViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}