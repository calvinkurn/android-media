package com.tokopedia.loginregister.common.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yoris Prayogo on 27/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SeamlessLoginAnalytics {

    fun trackScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventClickLoginSeamless(label: String) {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE_SELLER,
                ACTION_CLICK_ON_BUTTON_LOGIN,
                label
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
    }

    fun eventClickLoginWithOtherAcc() {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE_SELLER,
                ACTION_CLICK_ON_BUTTON_ANOTHER_ACCS,
                ""
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
    }

    fun eventClickBack() {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE_SELLER,
                ACTION_CLICK_ON_BUTTON_BACK,
                ""
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
    }

    companion object {

        val SCREEN_SEAMLESS_LOGIN = "/oneclicklogin - seller"

        private val EVENT_CLICK_LOGIN = "clickLogin"

        private val CATEGORY_LOGIN_PAGE_SELLER = "one click login - seller"

        private val ACTION_CLICK_ON_BUTTON_LOGIN = "click on masuk"
        private val ACTION_CLICK_ON_BUTTON_ANOTHER_ACCS = "click on masuk ke akun lain"
        private val ACTION_CLICK_ON_BUTTON_BACK = "click on button back"

        val LABEL_CLICK = "click"
        val LABEL_SUCCESS = "success"
        val LABEL_FAILED = "failed -"

        val LOGIN_METHOD_SEAMLESS = "seamlessLogin"

    }
}