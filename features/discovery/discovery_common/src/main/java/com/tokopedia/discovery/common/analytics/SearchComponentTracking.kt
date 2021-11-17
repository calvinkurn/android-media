package com.tokopedia.discovery.common.analytics

import com.tokopedia.track.interfaces.Analytics

interface SearchComponentTracking {
    fun impress(analytics: Analytics)
    fun click(analytics: Analytics)
    fun clickOtherAction(analytics: Analytics)
}