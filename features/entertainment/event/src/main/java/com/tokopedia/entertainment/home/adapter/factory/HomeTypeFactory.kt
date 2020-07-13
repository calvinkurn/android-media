package com.tokopedia.entertainment.home.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.*

/**
 * @author by errysuprayogi on 3/29/17.
 */
interface HomeTypeFactory {
    fun type(viewModel: BannerViewModel): Int
    fun type(viewModel: CategoryViewModel): Int
    fun type(viewModel: EventCarouselViewModel): Int
    fun type(viewModel: EventGridViewModel): Int
    fun type(viewModel: EventLocationViewModel): Int
    fun createViewHolder(view: ViewGroup, viewType: Int): HomeEventViewHolder<*>
}