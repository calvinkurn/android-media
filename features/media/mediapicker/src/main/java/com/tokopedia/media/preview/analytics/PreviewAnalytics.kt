package com.tokopedia.media.preview.analytics

interface PreviewAnalytics {
    fun clickNextButton(
        entryPoint: String,
        buttonState: String
    )

    fun clickBackButton(
        entryPoint: String
    )

    fun clickRetakeButton(
        entryPoint: String,
        retakeState: String
    )

    fun clickDrawerThumbnail(
        entryPoint: String
    )
}