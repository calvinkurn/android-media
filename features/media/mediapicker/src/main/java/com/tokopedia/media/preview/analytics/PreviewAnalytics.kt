package com.tokopedia.media.preview.analytics

interface PreviewAnalytics {
    fun clickNextButton(
        buttonState: String
    )

    fun clickBackButton()

    fun clickRetakeButton(
        retakeState: String
    )

    fun clickDrawerThumbnail()
}