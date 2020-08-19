package com.tokopedia.entertainment.home.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.*

/**
 * @author by errysuprayogi on 3/29/17.
 */
interface HomeTypeFactory {
    fun type(model: BannerModel): Int
    fun type(model: CategoryModel): Int
    fun type(model: EventCarouselModel): Int
    fun type(model: EventGridModel): Int
    fun type(model: EventLocationModel): Int
    fun createViewHolder(view: ViewGroup, viewType: Int): HomeEventViewHolder<*>
}