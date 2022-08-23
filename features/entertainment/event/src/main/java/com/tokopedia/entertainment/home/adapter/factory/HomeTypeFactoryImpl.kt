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

    override fun type(model: LoadingHomeModel): Int {
        return LoadingHomeEventViewHolder.LAYOUT
    }

    override fun type(model: TickerModel): Int {
        return TickerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BannerEventViewHolder.LAYOUT -> {
                BannerEventViewHolder(view, trackingListener)
            }
            CategoryEventViewHolder.LAYOUT -> {
                CategoryEventViewHolder(view, trackingListener)
            }
            EventGridEventViewHolder.LAYOUT -> {
                EventGridEventViewHolder(view, trackingListener, clickGridListener)
            }
            EventCarouselEventViewHolder.LAYOUT -> {
                EventCarouselEventViewHolder(view, trackingListener, clickCarouselListener)
            }
            EventLocationEventViewHolder.LAYOUT -> {
                EventLocationEventViewHolder(view, trackingListener)
            }
            LoadingHomeEventViewHolder.LAYOUT -> {
                LoadingHomeEventViewHolder(view)
            }
            TickerViewHolder.LAYOUT -> {
                TickerViewHolder(view)
            }
            else -> super.createViewHolder(view, type)
        }
    }
}