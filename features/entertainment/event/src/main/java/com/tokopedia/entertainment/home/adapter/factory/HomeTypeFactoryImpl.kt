package com.tokopedia.entertainment.home.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.*
import com.tokopedia.entertainment.home.adapter.viewmodel.*
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking

/**
 * Author errysuprayogi on 29,January,2020
 */
class HomeTypeFactoryImpl(val action:((data: EventItemModel,
                                       onSuccess: (EventItemModel)->Unit,
                                       onError: (Throwable)->Unit) -> Unit), val analytic:EventHomePageTracking) : HomeTypeFactory {

    override fun type(model: BannerModel): Int {
        return BannerEventViewHolder.LAYOUT
    }

    override fun type(model: CategoryModel): Int {
        return CategoryEventViewHolder.LAYOUT
    }

    override fun type(model: EventCarouselModel): Int {
        return EventCarouselEventViewHolder.LAYOUT
    }

    override fun type(model: EventGridModel): Int {
        return EventGridEventViewHolder.LAYOUT
    }

    override fun type(model: EventLocationModel): Int {
        return EventLocationEventViewHolder.LAYOUT
    }

    override fun createViewHolder(view: ViewGroup, type: Int): HomeEventViewHolder<*> {
        val creatEventViewHolder: HomeEventViewHolder<*>
        creatEventViewHolder = if (type == BannerEventViewHolder.LAYOUT) {
            BannerEventViewHolder(view,analytic)
        } else if (type == CategoryEventViewHolder.LAYOUT) {
            CategoryEventViewHolder(view,analytic)
        } else if (type == EventGridEventViewHolder.LAYOUT) {
            EventGridEventViewHolder(view, action,analytic)
        } else if (type == EventCarouselEventViewHolder.LAYOUT) {
            EventCarouselEventViewHolder(view, action,analytic)
        } else if (type == EventLocationEventViewHolder.LAYOUT) {
            EventLocationEventViewHolder(view,analytic)
        } else {
            throw TypeNotSupportedException.create("Layout not supported")
        }
        return creatEventViewHolder
    }
}