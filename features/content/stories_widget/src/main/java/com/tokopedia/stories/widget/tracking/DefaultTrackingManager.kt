package com.tokopedia.stories.widget.tracking

import com.tokopedia.stories.widget.domain.StoriesEntryPoint

/**
 * Created by kenny.hadisaputra on 23/08/23
 */
class DefaultTrackingManager(private val entryPoint: StoriesEntryPoint) : TrackingManager {

    val eventCategory: String = when (entryPoint) {
        StoriesEntryPoint.ShopPage -> "shop page - buyer"
        StoriesEntryPoint.ProductDetail -> "product detail page"
        StoriesEntryPoint.TopChatList -> "inbox-chat"
        StoriesEntryPoint.TopChatRoom -> "message room"
    }

    override fun impressEntryPoints(key: StoriesEntryPoint) {
    }

    override fun clickEntryPoints(key: StoriesEntryPoint) {
    }
}
