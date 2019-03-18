package com.tokopedia.gallery.tracking

import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.track.interfaces.ContextAnalytics

object ImageReviewGalleryTracking {

    private val CLICK_PDP = "clickPDP"
    private val PRODUCT_DETAIL_PAGE = "product detail page"

    fun eventClickReviewGalleryItem(context: Context, productId: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - review gallery on review gallery list page",
                productId
        ))
    }
}
