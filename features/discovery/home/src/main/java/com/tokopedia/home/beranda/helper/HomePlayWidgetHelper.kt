package com.tokopedia.home.beranda.helper

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 29/05/23
 */
class HomePlayWidgetHelper @Inject constructor() {

    private val carouselLayoutType = listOf(
        DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_NO_PRODUCT,
        DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_WITH_PRODUCT
    )

    fun isCarousel(layout: String): Boolean {
        return layout in carouselLayoutType
    }

    fun isCarouselVariantWithProduct(layout: String): Boolean {
        return layout == DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_WITH_PRODUCT
    }
}
