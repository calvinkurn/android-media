package com.tokopedia.search.result.presentation.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselOptionInfoViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselOptionListViewHolder
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener

class InspirationCarouselOptionAdapterTypeFactory(
        private val suggestionTopShopListener: InspirationCarouselListener
) : BaseAdapterTypeFactory(), InspirationCarouselOptionTypeFactory {

    override fun type(type: String): Int {
        return when(type) {
            LAYOUT_INSPIRATION_CAROUSEL_INFO -> InspirationCarouselOptionInfoViewHolder.LAYOUT
            else -> InspirationCarouselOptionListViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InspirationCarouselOptionListViewHolder.LAYOUT -> InspirationCarouselOptionListViewHolder(parent, suggestionTopShopListener)
            InspirationCarouselOptionInfoViewHolder.LAYOUT -> InspirationCarouselOptionInfoViewHolder(parent, suggestionTopShopListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}