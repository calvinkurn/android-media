package com.tokopedia.gamification.giftbox.data.di.modules

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.gamification.giftbox.data.di.GAMI_GIFT_DAILY_PLT_NETWORK_METRICS
import com.tokopedia.gamification.giftbox.data.di.GAMI_GIFT_DAILY_PLT_PREPARE_METRICS
import com.tokopedia.gamification.giftbox.data.di.GAMI_GIFT_DAILY_PLT_RENDER_METRICS

class PltModule {

    fun providePerfInterface(): PageLoadTimePerformanceInterface {
        return PageLoadTimePerformanceCallback(
                GAMI_GIFT_DAILY_PLT_PREPARE_METRICS,
                GAMI_GIFT_DAILY_PLT_NETWORK_METRICS,
                GAMI_GIFT_DAILY_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
    }
}