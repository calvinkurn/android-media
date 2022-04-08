package com.tokopedia.media.preview.analytics

interface PreviewAnalytics {
    fun clickBackButton()
    fun clickDrawerThumbnail()
    fun clickNextButton(buttonState: String)
    fun clickRetakeButton(retakeState: String)
}