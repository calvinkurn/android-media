package com.tokopedia.entertainment.adapter.factory

import android.view.ViewGroup
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.BannerViewModel
import com.tokopedia.entertainment.adapter.viewmodel.CategoryViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel
import com.tokopedia.entertainment.adapter.viewmodel.EventGridViewModel

/**
 * @author by errysuprayogi on 3/29/17.
 */
interface HomeTypeFactory {
    fun type(viewModel: BannerViewModel): Int
    fun type(viewModel: CategoryViewModel): Int
    fun type(viewModel: EventCarouselViewModel): Int
    fun type(viewModel: EventGridViewModel): Int
    fun createViewHolder(view: ViewGroup, viewType: Int): HomeViewHolder<*>
}