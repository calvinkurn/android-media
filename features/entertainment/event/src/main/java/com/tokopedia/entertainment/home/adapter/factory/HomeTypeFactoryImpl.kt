package com.tokopedia.entertainment.home.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewholder.*
import com.tokopedia.entertainment.home.adapter.viewmodel.*

/**
 * Author errysuprayogi on 29,January,2020
 */
class HomeTypeFactoryImpl(private val trackingListener: TrackingListener,
                          private val clickGridListener: EventGridEventViewHolder.ClickGridListener,
                          private val clickCarouselListener: EventCarouselEventViewHolder.ClickCarouselListener
) : BaseAdapterTypeFactory(),HomeTypeFactory {

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

    override fun type(model: EmptyHomeModel): Int {
        return EmptyHomeEventViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return if (type == BannerEventViewHolder.LAYOUT) {
            BannerEventViewHolder(view, trackingListener)
        } else if (type == CategoryEventViewHolder.LAYOUT) {
            CategoryEventViewHolder(view, trackingListener)
        } else if (type == EventGridEventViewHolder.LAYOUT) {
            EventGridEventViewHolder(view, trackingListener, clickGridListener)
        } else if (type == EventCarouselEventViewHolder.LAYOUT) {
            EventCarouselEventViewHolder(view, trackingListener, clickCarouselListener)
        } else if (type == EventLocationEventViewHolder.LAYOUT) {
            EventLocationEventViewHolder(view, trackingListener)
        } else if (type == EmptyHomeEventViewHolder.LAYOUT){
            EmptyHomeEventViewHolder(view)
        } else super.createViewHolder(view, type)
    }
}