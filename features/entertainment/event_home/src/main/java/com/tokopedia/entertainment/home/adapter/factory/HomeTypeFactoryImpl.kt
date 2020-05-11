package com.tokopedia.entertainment.home.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.*
import com.tokopedia.entertainment.home.adapter.viewmodel.*

/**
 * Author errysuprayogi on 29,January,2020
 */
class HomeTypeFactoryImpl(val action:((data: EventItemModel,
                                       onSuccess: (EventItemModel)->Unit,
                                       onError: (Throwable)->Unit) -> Unit)) : HomeTypeFactory {

    override fun type(viewModel: BannerViewModel): Int {
        return BannerEventViewHolder.LAYOUT
    }

    override fun type(viewModel: CategoryViewModel): Int {
        return CategoryEventViewHolder.LAYOUT
    }

    override fun type(viewModel: EventCarouselViewModel): Int {
        return EventCarouselEventViewHolder.LAYOUT
    }

    override fun type(viewModel: EventGridViewModel): Int {
        return EventGridEventViewHolder.LAYOUT
    }

    override fun type(viewModel: EventLocationViewModel): Int {
        return EventLocationEventViewHolder.LAYOUT
    }

    override fun createViewHolder(view: ViewGroup, type: Int): HomeEventViewHolder<*> {
        val creatEventViewHolder: HomeEventViewHolder<*>
        creatEventViewHolder = if (type == BannerEventViewHolder.LAYOUT) {
            BannerEventViewHolder(view)
        } else if (type == CategoryEventViewHolder.LAYOUT) {
            CategoryEventViewHolder(view)
        } else if (type == EventGridEventViewHolder.LAYOUT) {
            EventGridEventViewHolder(view, action)
        } else if (type == EventCarouselEventViewHolder.LAYOUT) {
            EventCarouselEventViewHolder(view, action)
        } else if (type == EventLocationEventViewHolder.LAYOUT) {
            EventLocationEventViewHolder(view)
        } else {
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return creatEventViewHolder
    }
}