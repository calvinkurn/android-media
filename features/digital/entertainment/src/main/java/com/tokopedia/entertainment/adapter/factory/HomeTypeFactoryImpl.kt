package com.tokopedia.entertainment.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewholder.BannerViewHolder
import com.tokopedia.entertainment.adapter.viewholder.CategoryViewHolder
import com.tokopedia.entertainment.adapter.viewholder.EventCarouselViewHolder
import com.tokopedia.entertainment.adapter.viewholder.EventGridViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.BannerViewModel
import com.tokopedia.entertainment.adapter.viewmodel.CategoryViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventGridViewModel

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
        } else {
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return creatViewHolder
    }
}