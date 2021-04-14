package com.tokopedia.shop.score.performance.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

/**
 * SHOP SCORE REVAMP
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/804
 */
object ShopPerformanceTracking {
    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

}