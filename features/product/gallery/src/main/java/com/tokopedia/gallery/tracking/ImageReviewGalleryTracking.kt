package com.tokopedia.gallery.tracking

import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker

object ImageReviewGalleryTracking {

    private val CLICK_PDP = "clickPDP"
    private val PRODUCT_DETAIL_PAGE = "product detail page"

    fun eventClickReviewGalleryItem(context: Context, productId: String) {
        if (context.applicationContext !is AbstractionRouter) {
            return
        }
        val tracker = (context.applicationContext as AbstractionRouter).analyticTracker
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - review gallery on review gallery list page",
                productId
        )
    }
}
