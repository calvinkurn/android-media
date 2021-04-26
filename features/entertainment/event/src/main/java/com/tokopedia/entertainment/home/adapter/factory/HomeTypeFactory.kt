package com.tokopedia.entertainment.home.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.entertainment.home.adapter.viewmodel.*

/**
 * @author by errysuprayogi on 3/29/17.
 */
interface HomeTypeFactory: AdapterTypeFactory {
    fun type(model: BannerModel): Int
    fun type(model: CategoryModel): Int
    fun type(model: EventCarouselModel): Int
    fun type(model: EventGridModel): Int
    fun type(model: EventLocationModel): Int
    fun type(model: EmptyHomeModel): Int
}