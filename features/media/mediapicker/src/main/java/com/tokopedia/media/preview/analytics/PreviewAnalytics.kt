package com.tokopedia.media.preview.analytics

interface PreviewAnalytics {
    fun clickBackButton()
    fun clickDrawerThumbnail()
    fun clickNextButton(buttonState: String,  listImage: List<Triple<String,String, Int>>)
    fun clickRetakeButton(retakeState: String)
}
