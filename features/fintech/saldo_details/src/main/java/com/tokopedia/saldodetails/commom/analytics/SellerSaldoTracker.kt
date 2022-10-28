package com.tokopedia.saldodetails.commom.analytics

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by @ilhamsuaib on 19/05/22.
 */

object SellerSaldoTracker {

    private const val EVENT_NAME = "clickPG"
    private const val EVENT_ACTION = "click download biaya layanan"
    private const val EVENT_CATEGORY = "saldo page"
    private const val BUSINESS_UNIT = "Physical Goods"
    private const val CURRENT_SITE = "tokopediamarketplace"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    fun sendDownloadCommissionEntryPointClickEvent() {

        val eventMap = TrackAppUtils.gtmData(
            EVENT_NAME, EVENT_CATEGORY, EVENT_ACTION, String.EMPTY
        )
        eventMap[KEY_BUSINESS_UNIT] = BUSINESS_UNIT
        eventMap[KEY_CURRENT_SITE] = CURRENT_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }
}