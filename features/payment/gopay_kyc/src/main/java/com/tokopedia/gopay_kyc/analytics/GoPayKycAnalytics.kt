package com.tokopedia.gopay_kyc.analytics

import com.tokopedia.gopay_kyc.di.GoPayKycScope
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

@GoPayKycScope
class GoPayKycAnalytics @Inject constructor(
    //private val userSession: dagger.Lazy<UserSession>
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendOpenScreenEvents() {

    }

    fun sendActionClickEvents(action: String, pageSource: String, label: String = "") {
        if (action.isEmpty()) return
        val map = TrackAppUtils.gtmData(
            GoPayKycConstants.Event.CLICK_PAYMENT,
            GoPayKycConstants.Category.PEMUDA_KYC_PAGE,
            action,
            label
        )
        map[GoPayKycConstants.PAGE_SOURCE] = pageSource
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[GoPayKycConstants.KEY_BUSINESS_UNIT] = GoPayKycConstants.VALUE_BUSINESS_UNIT
        map[GoPayKycConstants.KEY_CURRENT_SITE] = GoPayKycConstants.VALUE_CURRENT_SITE
        //map[GoPayKycConstants.KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }
}