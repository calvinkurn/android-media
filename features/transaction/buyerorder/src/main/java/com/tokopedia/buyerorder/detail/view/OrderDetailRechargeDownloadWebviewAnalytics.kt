package com.tokopedia.buyerorder.detail.view

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by furqan on 21/01/2021
 */
class OrderDetailRechargeDownloadWebviewAnalytics {

    fun rechargeDownloadOpenScreen(isLoggedIn: Boolean, categoryName: String, productName: String, userId: String) {
        val event = TrackAppUtils.gtmData(EVENT_NAME_OPEN_SCREEN,
                EVENT_CATEGORY_RECHARGE_INVOICE,
                EVENT_ACTION_RECHARGE_INVOICE,
                String.format("%s - %s", categoryName, productName))

        event[SCREEN_NAME] = SCREEN_NAME_INVOICE
        event[IS_LOGGED_IN_STATUS] = isLoggedIn.toString()
        event[CURRENT_SITE] = CURRENT_SITE_RECHARGE
        event[BUSINESS_UNIT] = BUSINESS_UNIT_RECHARGE
        event[USER_ID] = userId

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun rechargeInvoiceClickDownload(categoryName: String, productName: String, userId: String) {
        val event = TrackAppUtils.gtmData(EVENT_NAME_CLICK_DOWNLOAD,
                EVENT_CATEGORY_RECHARGE_INVOICE,
                EVENT_ACTION_RECHARGE_DOWNLOAD,
                String.format("%s - %s", categoryName, productName))

        event[CURRENT_SITE] = CURRENT_SITE_RECHARGE
        event[BUSINESS_UNIT] = BUSINESS_UNIT_RECHARGE
        event[USER_ID] = userId

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }


    companion object {
        private const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        private const val BUSINESS_UNIT = "businessUnit"
        private const val CURRENT_SITE = "currentSite"
        private const val SCREEN_NAME = "screenName"
        private const val USER_ID = "userId"

        private const val SCREEN_NAME_INVOICE = "/invoice-page-digital"

        private const val BUSINESS_UNIT_RECHARGE = "recharge"
        private const val CURRENT_SITE_RECHARGE = "tokopediadigital"

        private const val EVENT_NAME_OPEN_SCREEN = "openScreen"
        private const val EVENT_NAME_CLICK_DOWNLOAD = "clickCheckout"

        private const val EVENT_CATEGORY_RECHARGE_INVOICE = "digital - invoice page"

        private const val EVENT_ACTION_RECHARGE_INVOICE = "impression invoice page"
        private const val EVENT_ACTION_RECHARGE_DOWNLOAD = "click print or download"

    }

}