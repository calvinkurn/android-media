package com.tokopedia.home_account.analytics

import android.content.Context
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.Analytics.ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.SETTING
import com.tokopedia.home_account.AccountConstants.Analytics.SHOP
import com.tokopedia.home_account.AccountConstants.Analytics.USER
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yoris Prayogo on 27/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountAnalytics(val context: Context, val userSessionInterface: UserSessionInterface) {

    fun eventClickSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE, String.format("%s %s", USER, SETTING), String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ))
    }

    fun eventClickAccountSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE, String.format("%s %s", ACCOUNT, SETTING), String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ))
    }


    fun eventClickPaymentSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE, String.format("%s %s", SHOP, SETTING), String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ))
    }

    fun eventClickToggleOnGeolocation(context: Context?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics?.sendGeneralEvent(
                "clickHomePage",
                "homepage",
                "click toggle on geolocation",
                ""
        )
    }

    fun eventClickToggleOffGeolocation(context: Context?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics?.sendGeneralEvent(
                "clickHomePage",
                "homepage",
                "click toggle off geolocation",
                ""
        )
    }

}
