package com.tokopedia.loginregister.common.analytics

import android.os.Build
import com.tokopedia.config.GlobalConfig
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
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

    // Tracker ID: 50566 MainApp, 50571 SellerApp
    fun sendSellerClickIndividualEvent (shopId: String, userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) "50571" else "50566"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click individual")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50567
    fun sendSellerClickRegisterToOsEvent () {
        val trackerId = if (GlobalConfig.isSellerApp()) "50572" else "50567"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click register to os")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId("")
            .setUserId("")
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50568
    fun sendSellerClickTetapBukaDiPerangkatIniEvent (shopId: String, userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) "50573" else "50568"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click tetap buka di perangkat Ini")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50569
    fun sendSellerClickDismissTheKycPromptEvent (shopId: String, userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) "50574" else "50569"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click dismiss the kyc prompt")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50693
    fun sendSellerClickRefreshStatusEvent (shopId: String, userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) "50695" else "50693"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click refresh status")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50694
    fun sendSellerClickVerifikasiUlangEvent (shopId: String, userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) "50696" else "50694"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click verifikasi ulang")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50705
    fun sendSellerClickToSellerEducationMaterialsEvent (shopId: String, userId: String) {
        val trackerId = if (GlobalConfig.isSellerApp()) "50708" else "50705"
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click to seller education materials")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", trackerId)
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
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
