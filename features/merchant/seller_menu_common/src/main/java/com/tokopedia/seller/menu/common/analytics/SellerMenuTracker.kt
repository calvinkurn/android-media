package com.tokopedia.seller.menu.common.analytics

import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 04/09/20
 */

//data layer : https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=412390831
class SellerMenuTracker(
        private val analytics: Analytics,
        private val userSession: UserSessionInterface
) {

    companion object {

        // name
        private const val EVENT_CLICK_SHOP_ACCOUNT = "clickShopAccount"
        private const val EVENT_CLICK_TOP_NAV = "clickTopNav"
        private const val EVENT_VIEW_SHOP_ACCOUNT = "viewShopAccountIris"

        // category
        private const val CATEGORY_MA_SHOP_ACCOUNT = "ma - shop account"
        private const val CATEGORY_SETTINGS = "settings"
        private const val CATEGORY_TOP_NAV = "top nav"

        // action
        private const val ACTION_CLICK_SHOP_ACCOUNT = "click shop account"
        private const val ACTION_CLOSE_SHOP_ACCOUNT = "close shop account"
        private const val ACTION_CLICK_INBOX = "click inbox"
        private const val ACTION_CLICK_NOTIFICATION = "click notification"
        private const val ACTION_CLICK_SHOP_PICTURE = "click shop picture"
        private const val ACTION_CLICK_SHOP_NAME = "click shop name"
        private const val ACTION_CLICK_SHOP_SCORE = "click shop score"
        private const val ACTION_CLICK_SHOP_TYPE = "click shop type"
        private const val ACTION_CLICK_SHOP_BALANCE = "click saldo"
        private const val ACTION_CLICK_ORDER_HISTORY = "penjualan - riwayat penjualan"
        private const val ACTION_CLICK_ORDER_NEW = "penjualan - pesanan baru"
        private const val ACTION_CLICK_ORDER_READY_TO_SHIP = "penjualan - siap dikirim"
        private const val ACTION_IMPRESSION_SHOP_STATUS = "impression shop status"
        private const val ACTION_ADD_PRODUCT = "produk - tambah produk"
        private const val ACTION_PRODUCT_LIST = "produk - daftar produk"
        private const val ACTION_BUYER_CLICK_REVIEW = "pembeli - click review"
        private const val ACTION_BUYER_CLICK_DISCUSSION = "pembeli - click discussion"
        private const val ACTION_BUYER_CLICK_COMPLAIN = "pembeli - click complain"
        private const val ACTION_OTHER_CLICK_SELLER_EDU = "lainnya - click pusat edukasi seller"
        private const val ACTION_OTHER_CLICK_TOKOPEDIA_CARE = "lainnya - click tokopedia care"
        private const val ACTION_OTHER_CLICK_SHOP_SETTINGS = "lainnya - click shop settings"
        private const val ACTION_SA_CLICK_SHOP_STAT = "sa - click shop stat"
        private const val ACTION_SA_CLICK_ADS_AND_PROMO = "sa - click ads and promos"
        private const val ACTION_SA_CLICK_POST_FEED = "sa - click post feed"
        private const val ACTION_SA_CLICK_FINANCIAL_SERVICES = "sa - click financial services"
        private const val ACTION_CLICK_BACK_ARROW = "click back arrow"
        private const val ACTION_CLICK_BASIC_INFO = "click shop settings - informasi dasar"
        private const val ACTION_CLICK_SHOP_NOTE = "click shop settings - catatan toko"
        private const val ACTION_CLICK_SHOP_SCHEDULE = "click shop settings - jam buka tutup toko"
        private const val ACTION_CLICK_SHOP_LOCATION = "click shop settings - tambah dan ubah lokasi toko"
        private const val ACTION_CLICK_SHIPPING = "click shop settings - atur layanan pengiriman"
        private const val ACTION_CLICK_NOTIFICATION_SETTINGS = "click shop settings - atur notifikasi penjual"

        // label
        private const val LABEL_CREATE_SHOP = "create shop"
        private const val LABEL_MY_SHOP = "myshop"

        // custom dimension
        private const val TOKOPEDIA_MARKET_PALCE = "tokopediamarketplace"
        private const val PHYSICAL_GOODS = "physical goods"
        private const val SHOP_TYPE_RM = "RM"
        private const val SHOP_TYPE_PM = "PM"
        private const val SHOP_TYPE_OS = "OS"
        private const val SHOP_STATUS_UPGRADE = "upgrade"
        private const val SHOP_STATUS_VERIFICATION = "verification"
        private const val SHOP_STATUS_ACTIVE = "active"
        private const val SHOP_STATUS_NOT_ACTIVE = "not active"
        private const val SHOP_STATUS_ON_VERIFICATION = "on verification"
        private const val SHOP_STATUS_OS = "official store"

        // key
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_USER_ID = "userId"
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_SCREEN_NAME = "screenName"

        // screen
        private const val SCREEN_NAME_ACCOUNT = "/account"

        // etc
        private const val UNDEFINED = "undefined"
    }

    fun sendEventViewShopAccount(shopInfo: SettingShopInfoUiModel) {
        val shopType = getShopType()
        val shopStatus = getShopStatus(shopInfo)
        val eventLabel = "$shopType - $shopStatus"

        val event = TrackAppUtils.gtmData(
            EVENT_VIEW_SHOP_ACCOUNT,
            CATEGORY_MA_SHOP_ACCOUNT,
            ACTION_IMPRESSION_SHOP_STATUS,
            eventLabel
        )

        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickMyShop() {
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_SHOP_ACCOUNT,
            CATEGORY_MA_SHOP_ACCOUNT,
            ACTION_CLICK_SHOP_ACCOUNT,
            LABEL_MY_SHOP
        )

        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }

    fun sendEventCreateShop() {
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_SHOP_ACCOUNT,
            CATEGORY_MA_SHOP_ACCOUNT,
            ACTION_CLICK_SHOP_ACCOUNT,
            LABEL_CREATE_SHOP
        )

        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }

    fun sendEventCloseShopAccount() {
        val event = createMenuItemEvent(ACTION_CLOSE_SHOP_ACCOUNT)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickInbox() {
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_TOP_NAV,
            CATEGORY_TOP_NAV,
            ACTION_CLICK_INBOX,
            ""
        )

        event[KEY_SCREEN_NAME] = SCREEN_NAME_ACCOUNT
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickNotification() {
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_TOP_NAV,
            CATEGORY_TOP_NAV,
            ACTION_CLICK_NOTIFICATION,
            ""
        )

        event[KEY_SCREEN_NAME] = SCREEN_NAME_ACCOUNT
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopPicture() {
        val event = createMenuItemEvent(ACTION_CLICK_SHOP_PICTURE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopName() {
        val event = createMenuItemEvent(ACTION_CLICK_SHOP_NAME)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopScore() {
        val event = createMenuItemEvent(ACTION_CLICK_SHOP_SCORE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopType() {
        val label = getShopType()
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_SHOP_ACCOUNT,
            CATEGORY_MA_SHOP_ACCOUNT,
            ACTION_CLICK_SHOP_TYPE,
            label
        )

        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickSaldoBalance() {
        val event = createMenuItemEvent(ACTION_CLICK_SHOP_BALANCE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickOrderHistory() {
        val event = createMenuItemEvent(ACTION_CLICK_ORDER_HISTORY)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickOrderNew() {
        val event = createMenuItemEvent(ACTION_CLICK_ORDER_NEW)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickOrderReadyToShip() {
        val event = createMenuItemEvent(ACTION_CLICK_ORDER_READY_TO_SHIP)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventAddProductClick() {
        val event = createMenuItemEvent(ACTION_ADD_PRODUCT)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickProductList() {
        val event = createMenuItemEvent(ACTION_PRODUCT_LIST)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickReview() {
        val event = createMenuItemEvent(ACTION_BUYER_CLICK_REVIEW)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickDiscussion() {
        val event = createMenuItemEvent(ACTION_BUYER_CLICK_DISCUSSION)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickComplain() {
        val event = createMenuItemEvent(ACTION_BUYER_CLICK_COMPLAIN)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickSellerEdu() {
        val event = createMenuItemEvent(ACTION_OTHER_CLICK_SELLER_EDU)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickTokopediaCare() {
        val event = createMenuItemEvent(ACTION_OTHER_CLICK_TOKOPEDIA_CARE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopSettings() {
        val event = createMenuItemEvent(ACTION_OTHER_CLICK_SHOP_SETTINGS)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopStatistic() {
        val event = createMenuItemEvent(ACTION_SA_CLICK_SHOP_STAT)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickCentralizePromo() {
        val event = createMenuItemEvent(ACTION_SA_CLICK_ADS_AND_PROMO)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickFeedAndPlay() {
        val event = createMenuItemEvent(ACTION_SA_CLICK_POST_FEED)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickFintech() {
        val event = createMenuItemEvent(ACTION_SA_CLICK_FINANCIAL_SERVICES)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickBackArrow() {
        val event = createSettingsItemEvent(ACTION_CLICK_BACK_ARROW)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickBasicInformation() {
        val event = createSettingsItemEvent(ACTION_CLICK_BASIC_INFO)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShopNotes() {
        val event = createSettingsItemEvent(ACTION_CLICK_SHOP_NOTE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickSchedule() {
        val event = createSettingsItemEvent(ACTION_CLICK_SHOP_SCHEDULE)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickLocation() {
        val event = createSettingsItemEvent(ACTION_CLICK_SHOP_LOCATION)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickShipping() {
        val event = createSettingsItemEvent(ACTION_CLICK_SHIPPING)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventClickNotificationSettings() {
        val event = createSettingsItemEvent(ACTION_CLICK_NOTIFICATION_SETTINGS)
        analytics.sendGeneralEvent(event)
    }

    fun sendEventOpenScreen(screeName: String) {
        analytics.sendScreenAuthenticated(screeName)
    }

    private fun createMenuItemEvent(actionName: String): Map<String, Any> {
        val event = TrackAppUtils.gtmData(
                EVENT_CLICK_SHOP_ACCOUNT,
                CATEGORY_MA_SHOP_ACCOUNT,
                actionName,
                ""
        )
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS
        return event
    }

    private fun createSettingsItemEvent(actionName: String): Map<String, Any> {
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_SHOP_ACCOUNT,
            CATEGORY_SETTINGS,
            actionName,
            ""
        )
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PALCE
        event[KEY_USER_ID] = userSession.userId
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS
        return event
    }

    private fun getShopType(): String {
        return when {
            userSession.isShopOfficialStore -> SHOP_TYPE_OS
            userSession.isGoldMerchant -> SHOP_TYPE_PM
            !userSession.isGoldMerchant -> SHOP_TYPE_RM
            else -> UNDEFINED
        }
    }

    private fun getShopStatus(shopInfo: SettingShopInfoUiModel): String {
        return when(shopInfo.shopStatusUiModel?.shopType) {
            is PowerMerchantStatus.Active -> SHOP_STATUS_ACTIVE
            is PowerMerchantStatus.NotActive -> SHOP_STATUS_NOT_ACTIVE
            is RegularMerchant.NeedUpgrade -> SHOP_STATUS_UPGRADE
            is RegularMerchant.NeedVerification -> SHOP_STATUS_VERIFICATION
            is PowerMerchantStatus.OnVerification -> SHOP_STATUS_ON_VERIFICATION
            is ShopType.OfficialStore -> SHOP_STATUS_OS
            else -> UNDEFINED
        }
    }
}