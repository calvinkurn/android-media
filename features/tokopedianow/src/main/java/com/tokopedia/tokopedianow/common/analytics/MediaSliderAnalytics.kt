package com.tokopedia.tokopedianow.common.analytics

interface MediaSliderAnalytics {

    fun trackImageImpression(position: Int)
    fun trackClickImage(position: Int)
    fun trackVideoImpression(position: Int)
    fun trackClickVideo(position: Int)
    fun trackClickFullscreen()
}