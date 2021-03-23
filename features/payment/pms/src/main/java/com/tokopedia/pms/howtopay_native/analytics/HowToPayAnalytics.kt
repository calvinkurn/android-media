package com.tokopedia.pms.howtopay_native.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class HowToPayAnalytics @Inject constructor(val userSessionInterface: UserSessionInterface) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun eventOnScreenOpen(paymentType: String) {
        val map = TrackAppUtils.gtmData(OPEN_SCREEN,
                CATEGORY_HWP, IMPRESSION_HTP_PAGE,
                paymentType
        )
        map[BUSINESS_UNIT] = PAYMENT
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[SCREEN_NAME] = paymentType
        map[IS_LOGGED_IN_STATUS] = userSessionInterface.isLoggedIn.toString()
        map[USER_ID] = userSessionInterface.userId
        analyticTracker.sendGeneralEvent(map)
    }

    fun eventOnCopyCodeClick(paymentType: String){
        val map = TrackAppUtils.gtmData(CLICK_PAYMENT,
                CATEGORY_HWP, COPY_CODE, "")
        map[BUSINESS_UNIT] = PAYMENT
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[SCREEN_NAME] = paymentType
        map[IS_LOGGED_IN_STATUS] = userSessionInterface.isLoggedIn.toString()
        map[USER_ID] = userSessionInterface.userId
        analyticTracker.sendGeneralEvent(map)
    }

    fun eventOnScreenShotClick(paymentType: String){
        val map = TrackAppUtils.gtmData(CLICK_PAYMENT,
                CATEGORY_HWP, CLICK_SCREENSHOT, "")
        map[BUSINESS_UNIT] = PAYMENT
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[SCREEN_NAME] = paymentType
        map[IS_LOGGED_IN_STATUS] = userSessionInterface.isLoggedIn.toString()
        map[USER_ID] = userSessionInterface.userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object{
        const val CLICK_PAYMENT = "clickPayment"
        const val SCREEN_NAME  = "screenName"
        const val IS_LOGGED_IN_STATUS  = "isLoggedInStatus"
        const val OPEN_SCREEN  = "openScreen"
        const val CURRENT_SITE  = "currentSite"
        const val BUSINESS_UNIT  = "businessUnit"
        const val PAYMENT  = "Payment"
        const val TOKOPEDIA_MARKET_PLACE  = "tokopediamarketplace"
        const val USER_ID = "userId"
        const val CLICK_SCREENSHOT = "click screenshot"
        const val COPY_CODE = "salin va code"
        const val CATEGORY_HWP = "htp page"
        const val IMPRESSION_HTP_PAGE = "impression htp page"
    }

}