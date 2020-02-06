package com.tokopedia.entertainment.home.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.home.adapter.HomeViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.*
import com.tokopedia.entertainment.home.adapter.viewmodel.*

/**
 * Author errysuprayogi on 29,January,2020
 */
class HomeTypeFactoryImpl : HomeTypeFactory {

    override fun type(viewModel: BannerViewModel): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(viewModel: CategoryViewModel): Int {
        return CategoryViewHolder.LAYOUT
    }

    override fun type(viewModel: EventCarouselViewModel): Int {
        return EventCarouselViewHolder.LAYOUT
    }

    override fun type(viewModel: EventGridViewModel): Int {
        return EventGridViewHolder.LAYOUT
    }

    override fun type(viewModel: EventLocationViewModel): Int {
        return EventLocationViewHolder.LAYOUT
    }

    override fun createViewHolder(view: ViewGroup, type: Int): HomeViewHolder<*> {
        val creatViewHolder: HomeViewHolder<*>
        creatViewHolder = if (type == BannerViewHolder.LAYOUT) {
            BannerViewHolder(view)
        } else if (type == CategoryViewHolder.LAYOUT) {
            CategoryViewHolder(view)
        } else if (type == EventGridViewHolder.LAYOUT) {
            EventGridViewHolder(view)
        } else if (type == EventCarouselViewHolder.LAYOUT) {
            EventCarouselViewHolder(view)
        } else if (type == EventLocationViewHolder.LAYOUT) {
            EventLocationViewHolder(view)
        } else {
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return creatViewHolder
    }
}