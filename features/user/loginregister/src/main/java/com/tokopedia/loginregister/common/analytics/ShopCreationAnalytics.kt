package com.tokopedia.loginregister.common.analytics

import android.os.Build
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import timber.log.Timber

/**
 * Created by Ade Fulki on 2020-01-10.
 * ade.hadian@tokopedia.com
 */

class ShopCreationAnalytics {

    fun trackScreen(screenName: String) {
        Timber.w("""P2screenName = $screenName | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}""")
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventClickOpenShopLanding() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_LANDING_PAGE_CREATE_SHOP,
                ACTION_CLICK_OPEN_SHOP,
                LABEL_EMPTY
        ))
    }

    fun eventClickContinuePhoneShopCreation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_REGISTRATION_PAGE_USER,
                ACTION_CLICK_CONTINUE,
                LABEL_EMPTY
        ))
    }

    fun eventSuccessClickContinueNameShopCreation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_REGISTRATION_PAGE_SHOP,
                ACTION_CLICK_CONTINUE,
                LABEL_SUCCESS
        ))
    }

    fun eventFailedClickContinueNameShopCreation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_REGISTRATION_PAGE_SHOP,
                ACTION_CLICK_CONTINUE,
                LABEL_FAILED
        ))
    }

    fun eventClickBackLanding() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_LANDING_PAGE_CREATE_SHOP,
                ACTION_CLICK_BACK,
                LABEL_EMPTY
        ))
    }

    fun eventClickBackPhoneShopCreation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_REGISTRATION_PAGE_USER,
                ACTION_CLICK_BACK,
                LABEL_EMPTY
        ))
    }

    fun eventClickBackNameShopCreation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CREATE_SHOP,
                CATEGORY_REGISTRATION_PAGE_SHOP,
                ACTION_CLICK_BACK_ADD_NAME_REGISTRATION,
                LABEL_EMPTY
        ))
    }

    companion object {
        const val SCREEN_LANDING_SHOP_CREATION = "/buka toko"
        const val SCREEN_REGISTRATION_SHOP_CREATION = "Registration page"
        const val SCREEN_OPEN_SHOP_CREATION = "Open Shop page"

        private const val EVENT_CLICK_CREATE_SHOP = "clickCreateShop"

        private const val CATEGORY_LANDING_PAGE_CREATE_SHOP = "landing page - create shop"
        private const val CATEGORY_REGISTRATION_PAGE_USER = "registration page - user"
        private const val CATEGORY_REGISTRATION_PAGE_SHOP = "registration page - shop"

        private const val ACTION_CLICK_OPEN_SHOP = "click open shop"
        private const val ACTION_CLICK_BACK = "click back"
        private const val ACTION_CLICK_BACK_ADD_NAME_REGISTRATION = "click back - add name registration"
        private const val ACTION_CLICK_CONTINUE = "click continue"

        private const val LABEL_EMPTY = ""
        private const val LABEL_SUCCESS = "success"
        private const val LABEL_FAILED = "failed"
    }
}