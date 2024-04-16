package com.tokopedia.loginregister.common.analytics

import android.os.Build
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

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50566
    fun sendSellerClickKycEvent (shopId: String, userId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click kyc")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50566")
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
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click register to os")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50567")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50568
    fun sendSellerClickPelajariSelengkapnyaEvent (shopId: String, userId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click pelajari selengkapnya")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50568")
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
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click dismiss the kyc prompt")
            .setEventCategory("kyc onboard")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50569")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50570
    fun sendSellerClickVerificationEvent (shopId: String, userId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click verification")
            .setEventCategory("pm page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50570")
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
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click refresh status")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50693")
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
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click verifikasi ulang")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50694")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50700
    fun sendSellerClickBukaTokoEvent (shopId: String, userId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click buka toko")
            .setEventCategory("entry point")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50700")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50703
    fun sendSellerClickLanjutBukaTokoEvent (shopId: String, userId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click lanjut buka toko")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50703")
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
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click to seller education materials")
            .setEventCategory("kyc waiting state")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50705")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50713
    fun sendSellerClickCekProsesPengecekanEvent (shopId: String, userId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click cek proses pengecekan")
            .setEventCategory("os waiting page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50713")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4554
    // Tracker ID: 50714
    fun sendSellerClickCekTetapBukaDiPerangkatIniEvent (businessUnit: String, currentSite: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("seller click cek tetap buka di perangkat ini")
            .setEventCategory("os waiting page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "50714")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
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
