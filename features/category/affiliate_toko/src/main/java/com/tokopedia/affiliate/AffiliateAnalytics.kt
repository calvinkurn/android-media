package com.tokopedia.affiliate

import com.tokopedia.affiliate.AffiliateAnalytics.EventKeys.Companion.EVENT_PROMO_CLICK
import com.tokopedia.affiliate.AffiliateAnalytics.EventKeys.Companion.KEY_PROMOTIONS
import com.tokopedia.affiliate.AffiliateAnalytics.EventKeys.Companion.PROMO_CLICK
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object AffiliateAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendEvent(event: String, action: String, category: String,
                  label: String, userId : String) {
        HashMap<String,Any>().apply {
            put(EventKeys.KEY_EVENT,event)
            put(EventKeys.KEY_EVENT_ACTION,action)
            put(EventKeys.KEY_EVENT_CATEGORY,category)
            put(EventKeys.KEY_EVENT_LABEL,label)
            put(EventKeys.KEY_USER_ID,userId)
            put(EventKeys.KEY_BUSINESS_UNIT,EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun sendOpenScreenEvent(event: String, screenName: String, isLoggedIn: Boolean, userId : String) {
        HashMap<String,Any>().apply {
            put(EventKeys.KEY_EVENT,event)
            put(EventKeys.SCREEN_NAME,screenName)
            put(EventKeys.IS_LOGGED_IN,isLoggedIn.toString())
            put(EventKeys.KEY_USER_ID,userId)
            put(EventKeys.KEY_BUSINESS_UNIT,EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun trackEventImpression(
        event: String,
        action: String,
        category: String,
        userId: String,
        productId: String,
        shopId : String,
        productImage: String,
        position: Int,
        itemName: String
    ){
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[EventKeys.KEY_ITEM_ID] = productId
        productMap[EventKeys.KEY_CREATIVE_NAME] = productImage
        productMap[EventKeys.KEY_CREATIVE_SLOT] = (position + 1).toString()
        productMap[EventKeys.KEY_ITEM_NAME] = itemName
        list.add(productMap)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = HashMap<String,Any>()
        map[EventKeys.KEY_EVENT] = PROMO_CLICK
        map[EventKeys.KEY_EVENT_CATEGORY] = category
        map[EventKeys.KEY_EVENT_ACTION] = action
        map[EventKeys.KEY_EVENT_LABEL] = "$shopId - $productId"
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_USER_ID] = userId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendIcomeTracker(
        event: String,
        action: String,
        category: String,
        eventLabel: String,
        position: Int,
        itemId: String,
        userId: String
    ){
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[EventKeys.KEY_ITEM_ID] = itemId
        productMap[EventKeys.KEY_CREATIVE_NAME] = ""
        productMap[EventKeys.KEY_CREATIVE_SLOT] = (position + 1).toString()
        productMap[EventKeys.KEY_ITEM_NAME] = ItemKeys.AFFILIATE_TRANSACTION_PAGE
        list.add(productMap)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        val map = HashMap<String,Any>()
        map[EventKeys.KEY_EVENT] = event
        map[EventKeys.KEY_EVENT_CATEGORY] = category
        map[EventKeys.KEY_EVENT_ACTION] = action
        map[EventKeys.KEY_EVENT_LABEL] = eventLabel
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_USER_ID] = userId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    interface EventKeys {
        companion object {
            const val KEY_EVENT = "event"
            const val KEY_EVENT_CATEGORY = "eventCategory"
            const val KEY_EVENT_ACTION = "eventAction"
            const val KEY_EVENT_LABEL = "eventLabel"
            const val KEY_USER_ID = "userId"
            const val SCREEN_NAME = "screenName"
            const val IS_LOGGED_IN = "isLoggedInStatus"
            const val OPEN_SCREEN = "openScreen"

            const val KEY_BUSINESS_UNIT = "businessUnit"
            const val KEY_CURRENT_SITE = "currentSite"

            const val BUSINESS_UNIT_VALUE= "affiliate"
            const val CURRENT_SITE_VALUE = "tokopediamarketplace"

            const val EVENT_VALUE_CLICK = "clickAffiliate"
            const val EVENT_VALUE_VIEW = "viewAffiliateIris"

            const val KEY_CREATIVE_NAME = "creative_name"
            const val KEY_CREATIVE_SLOT = "creative_slot"
            const val KEY_ITEM_ID = "item_id"
            const val KEY_ITEM_NAME = "item_name"

            const val KEY_PROMOTIONS = "promotions"
            const val SELECT_CONTENT = "selectContent"
            const val PROMO_CLICK = "promoclick"

            const val KEY_ECOMMERCE = "ecommerce"
            const val EVENT_PROMO_CLICK = "promoClick"

            const val CLICK_REGISTER = "clickRegister"
            const val CLICK_PG = "clickPG"
            const val VIEW_ITEM = "view_item"
        }
    }

    interface CategoryKeys {
        companion object {
            const val PROMOSIKAN_SRP_B_S = "promosikan srp - bottom sheet"
            const val HOME_PORTAL_B_S = "home portal - bottom sheet"
            const val PROMOSIKAN_SRP = "promosikan srp"
            const val HOME_PORTAL = "home portal"
            const val PROMOSIKAN_PAGE = "promosikan page"
            const val PROMOSIKAN_BOTTOM_SHEET = "promosikan page - bottom sheet"
            const val PENDAPATAN_PAGE = "pendapatan page"
            const val REGISTRATION_PAGE = "registration page"
            const val AFFILIATE_REGISTRATION_PAGE = "affiliate registration page"
            const val AFFILIATE_REGISTRATION_PAGE_PROMOTION_CHANNEL= "affiliate registration page - promotion channel input"
            const val AFFILIATE_REG_T_ANC_C = "affiliate registration page - terms and condition"
            const val AFFILIATE_HOME_PAGE = "affiliate home page"
            const val AFFILIATE_HOME_PAGE_FILTER = "affiliate home page - filter date"
            const val AFFILIATE_HOME_PAGE_BOTTOM_SHEET = "affiliate home page - bottom sheet"
            const val AFFILIATE_PROMOSIKAN_PAGE = "affiliate promosikan page"
            const val AFFILIATE_PENDAPATAN_PAGE = "affiliate pendapatan page"
        }
    }

    interface ActionKeys {
        companion object {
            const val CLICK_SALIN_LINK = "click - salin link"
            const val CLICK_SALIN_LINK_PERNAH_DIABEL = "click - salin link - pernah dibeli"
            const val CLICK_SALIN_LINK_PERNAH_DILIHAT = "click - salin link - pernah dilihat"
            const val IMPRESSION_LINK_GEN_ERROR = "impression - link generation error"
            const val IMPRESSION_NOT_LINK_ERROR = "impression - not link error"
            const val IMPRESSION_NOT_FOUND_ERROR = "impression - not found error"
            const val IMPRESSION_NOT_ELIGIBLE = "impression - not eligible error"
            const val IMPRESSION_NOT_OS_PM_ERROR = "impression - not os pm error"
            const val IMPRESSION_PROMOSIKAN_SRP = "impression - promosikan srp"
            const val IMPRESSION_PROMOSIKAN_SRP_B_S = "impression - promosikan srp - bottom sheet"
            const val IMPRESSION_HOME_PORTAL = "impression - home portal"
            const val IMPRESSION_HOME_PORTAL_B_S = "impression - home portal - bottom sheet"
            const val CLICK_SEARCH = "click - search"
            const val HOME_NAV_BAR_CLICK = "click - home nav"
            const val PROMOSIKAN_NAV_BAR_CLICK = "click - promosikan nav"
            const val BANUTAN_NAV_BAR_CLICK = "click - bantuan nav"
            const val PENDAPATAN_NAV_BAR_CLICK = "click - pendapatan nav"
            const val CLICK_PROMOSIKAN_PERNAH_DIABEL = "click - promosikan - pernah dibeli"
            const val CLICK_PROMOSIKAN_PERNAH_DILIHAT = "click - promosikan - pernah dilihat"
            const val CLICK_TRANSACTION_CARD = "click - transaction card"
            const val CLICK_DAFTAR_SEKARANG = "click - daftar sekarang"
            const val CLICK_SELANJUTNYA = "click - selanjutnya"
            const val CLICK_DAFTAR = "click - daftar"
            const val CLICK_TARIK_SALDO = "click - tarik saldo"
            const val CLICK_KELUAR = "click - keluar"
            const val CLICK_MASUK = "click - masuk"
            const val CLICK_PELJARI = "click - pelajari"
            const val CLICK_TAMBAH_SOCIAL_MEDIA= "click - tambah social media"
            const val CLICK_BACK = "click - back"
            const val CLICK_SYARAT = "click - syarat dan ketentuan"
            const val CLICK_FILTER_DATE = "click - filter date"
            const val CLICK_SIMPAN = "click - simpan"
            const val CLICK_IMPRESSION_PRODUCT_PRODUK = "impression - product - produk yang dipromosikan"
            const val CLICK_PRODUCT_PRODUK_YANG= "click - product - produk yang dipromosikan"
            const val CLICK_SALIN_LINK_PRODUK_YANG_DIPROMOSIKAN = "click - salin link - produk yang dipromosikan"
            const val CLICK_GENERATED_LINK_HISTORY = "click - generated link history"
            const val CLICK_PERNAH_DIBELI_TAB = "click - pernah dibeli tab"
            const val CLICK_PERNAH_DILIHAT_TAB = "click - pernah dilihat tab"
            const val IMPRESSION_TRANSACTION_CARD = "impression - transaction card"
        }
    }

    interface ScreenKeys{
        companion object{
            const val AFFILIATE_HOME_SCREEN_NAME = "/affiliate portal - home page"
            const val AFFILIATE_LOGIN_SCREEN_NAME = "/affiliate portal - registration page - "
        }
    }

    interface ItemKeys{
        companion object{
            const val AFFILIATE_PERNAH_DIBEL = "/affiliate - promosikan pernah dibeli"
            const val AFFILIATE_PERNAH_DILIHAT = "/affiliate - promosikan pernah dilihat"
            const val AFFILIATE_TRANSACTION_PAGE = "/affiliate pendapatan page - transaction history"
        }
    }

    interface LabelKeys {
        companion object {
            const val INCOMING = "incoming"
            const val OUTGOING = "outgoing"
            const val LOGIN = "login"
            const val NON_LOGIN = "non login"
            const val DEPOSIT = "deposit"
            const val WITHDRAWAL = "withdrawal"
        }
    }

}