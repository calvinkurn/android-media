package com.tokopedia.home.beranda.helper

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 29/05/23
 */
class HomePlayWidgetHelper @Inject constructor() {

    private val carouselLayoutType = listOf(
        DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_VAR_1,
        DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_VAR_2,
    )

    fun isCarousel(layout: String): Boolean {
//        return layout in carouselLayoutType
        return true //todo("uncomment above logic")
    }

    fun isCarouselVariantWithProduct(layout: String): Boolean {
//        return layout == DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_VAR_2
        return true
    }
}
