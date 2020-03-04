package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.v2.BaseTracking.Value.LIST
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

object MixTopTracking : BaseTracking() {
    private class CustomAction{
        companion object{
            val IMPRESSION_ON_CAROUSEL_PRODUCT = Action.IMPRESSION_ON.format("list carousel product")
        }
    }

    private class CustomActionField{
        companion object{
            val LIST_CAROUSEL_PRODUCT = LIST.format("%s", "list carousel product", "%s")
        }
    }

    fun getMixTopView(channel: DynamicHomeChannel.Channels) = getBasicProductView(
        Event.PRODUCT_VIEW,
            Category.HOMEPAGE,
            CustomAction.IMPRESSION_ON_CAROUSEL_PRODUCT,
            Label.NONE,
            CustomActionField.LIST_CAROUSEL_PRODUCT.format(),
            listOf()
    )
}