package com.tokopedia.media.picker.common.analytics

import com.tokopedia.media.preview.analytics.PreviewAnalytics

class TestPreviewAnalytics : PreviewAnalytics {
    override fun clickBackButton() {}
    override fun clickDrawerThumbnail() {}
    override fun clickNextButton(buttonState: String) {}
    override fun clickRetakeButton(retakeState: String) {}
}