package com.tokopedia.discovery.common.analytics

import com.tokopedia.iris.Iris
import com.tokopedia.track.interfaces.Analytics

interface SearchComponentTracking {
    fun impress(iris: Iris)
    fun click(analytics: Analytics)
    fun clickOtherAction(analytics: Analytics)
}