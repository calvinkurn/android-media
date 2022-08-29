package com.tokopedia.commissionbreakdown.tracker

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by @ilhamsuaib on 19/05/22.
 */

object CommissionTracker {

    private const val EVENT_NAME = "clickPG"
    private const val EVENT_ACTION = "click download laporan"
    private const val EVENT_CATEGORY = "laporan biaya layanan"
    private const val BUSINESS_UNIT = "Physical Goods"
    private const val CURRENT_SITE = "tokopediamarketplace"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    fun sendDownloadCtaClickEvent() {
        val eventMap = TrackAppUtils.gtmData(
            EVENT_NAME, EVENT_CATEGORY, EVENT_ACTION, String.EMPTY
        )
        eventMap[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        eventMap[KEY_CURRENT_SITE] = CURRENT_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }
}