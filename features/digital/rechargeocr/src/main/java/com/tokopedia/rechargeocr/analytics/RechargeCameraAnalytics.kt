package com.tokopedia.rechargeocr.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class RechargeCameraAnalytics {

    fun scanIdCard(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOMEPAGE, CATEGORY_DIGITAL,
                ACTION_SCAN_CAMERA,
                errorMessage
        ))
    }

    companion object {
        private const val EVENT_CLICK_HOMEPAGE = "clickHomepage"
        private const val CATEGORY_DIGITAL = "digital - native"
        private const val ACTION_SCAN_CAMERA = "scan proses"
    }
}