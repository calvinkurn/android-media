package com.tokopedia.affiliate

import android.os.Bundle
import com.tokopedia.affiliate.AffiliateAnalytics.EventKeys.Companion.ITEMS
import com.tokopedia.affiliate.AffiliateAnalytics.EventKeys.Companion.ITEM_LIST
import com.tokopedia.affiliate.AffiliateAnalytics.EventKeys.Companion.KEY_PROMOTIONS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object AffiliateAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendEvent(
        event: String,
        action: String,
        category: String,
        label: String,
        userId: String
    ) {
        HashMap<String, Any>().apply {
            put(EventKeys.KEY_EVENT, event)
            put(EventKeys.KEY_EVENT_ACTION, action)
            put(EventKeys.KEY_EVENT_CATEGORY, category)
            put(EventKeys.KEY_EVENT_LABEL, label)
            put(EventKeys.KEY_USER_ID, userId)
            put(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun sendOpenScreenEvent(event: String, screenName: String, isLoggedIn: Boolean, userId: String) {
        HashMap<String, Any>().apply {
            put(EventKeys.KEY_EVENT, event)
            put(EventKeys.SCREEN_NAME, screenName)
            put(EventKeys.IS_LOGGED_IN, isLoggedIn.toString())
            put(EventKeys.KEY_USER_ID, userId)
            put(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun trackEventImpression(
        event: String,
        action: String,
        category: String,
        userId: String,
        productId: String?,
        position: Int,
        itemName: String?,
        label: String?,
        itemList: String = ""
    ) {
        val listBundle = Bundle().apply {
            putString(EventKeys.KEY_ITEM_ID, productId)
            putString(EventKeys.INDEX, (position).toString())
            putString(EventKeys.KEY_ITEM_NAME, itemName)
        }
        val bundle = Bundle().apply {
            putString(EventKeys.KEY_EVENT, event)
            putString(EventKeys.KEY_EVENT_CATEGORY, category)
            putString(EventKeys.KEY_EVENT_ACTION, action)
            putString(EventKeys.KEY_EVENT_LABEL, label)
            putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
            putParcelableArrayList(ITEMS, arrayListOf(listBundle))
            putString(EventKeys.KEY_USER_ID, userId)
            if (itemList.isNotEmpty()) {
                putString(ITEM_LIST, itemList)
            }
        }

        getTracker().sendEnhanceEcommerceEvent(event, bundle)
    }

    fun sendEducationTracker(
        event: String?,
        action: String?,
        category: String?,
        eventLabel: String? = null,
        position: Int? = null,
        itemId: String? = null,
        userId: String,
        creativeName: String? = null
    ) {
        val bundle = Bundle()
        val itemBundle = Bundle().apply {
            putString(EventKeys.KEY_CREATIVE_NAME, creativeName)
            if (position != null) {
                putString(EventKeys.KEY_CREATIVE_SLOT, (position + 1).toString())
            }
            putString(EventKeys.KEY_ITEM_ID, itemId)
            putString(EventKeys.KEY_ITEM_NAME, ItemKeys.AFFILIATE_EDUCATION_PAGE)
        }
        bundle.putString(EventKeys.KEY_EVENT, event)
        bundle.putString(EventKeys.KEY_EVENT_ACTION, action)
        bundle.putString(EventKeys.KEY_EVENT_CATEGORY, category)
        bundle.putString(EventKeys.KEY_EVENT_LABEL, eventLabel)
        bundle.putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
        bundle.putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
        bundle.putString(EventKeys.KEY_USER_ID, userId)
        bundle.putParcelableArrayList(KEY_PROMOTIONS, arrayListOf(itemBundle))

        getTracker().sendEnhanceEcommerceEvent(event, bundle)
    }

    fun sendIcomeTracker(
        event: String,
        action: String,
        category: String,
        eventLabel: String,
        position: Int,
        itemId: String,
        userId: String
    ) {
        val bundle = Bundle()
        val itemBundle = Bundle().apply {
            putString(EventKeys.KEY_ITEM_ID, itemId)
            putString(EventKeys.KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(EventKeys.KEY_ITEM_NAME, ItemKeys.AFFILIATE_TRANSACTION_PAGE)
        }
        bundle.putString(EventKeys.KEY_EVENT, event)
        bundle.putString(EventKeys.KEY_EVENT_CATEGORY, category)
        bundle.putString(EventKeys.KEY_EVENT_ACTION, action)
        bundle.putString(EventKeys.KEY_EVENT_LABEL, eventLabel)
        bundle.putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
        bundle.putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
        bundle.putString(EventKeys.KEY_USER_ID, userId)
        bundle.putParcelableArrayList(KEY_PROMOTIONS, arrayListOf(itemBundle))

        getTracker().sendEnhanceEcommerceEvent(event, bundle)
    }
    fun sendTickerEvent(
        event: String,
        action: String,
        category: String,
        eventLabel: String,
        position: Int,
        itemId: Long,
        itemName: String,
        userId: String
    ) {
        val bundle = Bundle()
        val itemBundle = Bundle().apply {
            putLong(EventKeys.KEY_ITEM_ID, itemId)
            putString(EventKeys.KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(EventKeys.KEY_ITEM_NAME, itemName)
        }
        bundle.putString(EventKeys.KEY_EVENT, event)
        bundle.putString(EventKeys.KEY_EVENT_CATEGORY, category)
        bundle.putString(EventKeys.KEY_EVENT_ACTION, action)
        bundle.putString(EventKeys.KEY_EVENT_LABEL, eventLabel)
        bundle.putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
        bundle.putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
        bundle.putString(EventKeys.KEY_USER_ID, userId)
        bundle.putParcelableArrayList(KEY_PROMOTIONS, arrayListOf(itemBundle))

        getTracker().sendEnhanceEcommerceEvent(event, bundle)
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

            const val BUSINESS_UNIT_VALUE = "affiliate"
            const val CURRENT_SITE_VALUE = "tokopediamarketplace"

            const val EVENT_VALUE_CLICK = "clickAffiliate"
            const val EVENT_VALUE_VIEW = "viewAffiliateIris"

            const val KEY_CREATIVE_NAME = "creative_name"
            const val KEY_CREATIVE_SLOT = "creative_slot"
            const val KEY_ITEM_ID = "item_id"
            const val KEY_ITEM_NAME = "item_name"
            const val ITEMS = "items"

            const val KEY_PROMOTIONS = "promotions"
            const val SELECT_CONTENT = "select_content"
            const val PROMO_CLICK = "promoclick"

            const val KEY_ECOMMERCE = "ecommerce"
            const val EVENT_PROMO_CLICK = "promoClick"

            const val CLICK_REGISTER = "clickRegister"
            const val CLICK_PG = "clickPG"
            const val VIEW_ITEM = "view_item"
            const val VIEW_ITEM_LIST = "view_item_list"
            const val ITEM_LIST = "item_list"
            const val CLICK_CONTENT = "clickContent"
            const val INDEX = "index"
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
            const val AFFILIATE_REGISTRATION_PAGE_PROMOTION_CHANNEL = "affiliate registration page - promotion channel input"
            const val AFFILIATE_REG_T_ANC_C = "affiliate registration page - terms and condition"
            const val AFFILIATE_HOME_PAGE = "affiliate home page"
            const val AFFILIATE_HOME_PAGE_LINK_HISTORY = "affiliate home page - generated link history"
            const val AFFILIATE_HOME_PAGE_GENERATED_LINK_HIST = "affiliate home page - generated link history"
            const val AFFILIATE_HOME_PAGE_FILTER = "affiliate home page - filter date"
            const val AFFILIATE_HOME_PAGE_BOTTOM_SHEET = "affiliate home page - bottom sheet"
            const val AFFILIATE_PROMOSIKAN_PAGE = "affiliate promosikan page"
            const val AFFILIATE_PENDAPATAN_PAGE = "affiliate pendapatan page"
            const val AFFILIATE_PENDAPATAN_PAGE_FILTER = "affiliate pendapatan page - filter date"
            const val AFFILIATE_PROMOSIKAN_BOTTOM_SHEET = "affiliate promosikan page - bottom sheet"
            const val AFFILIATE_HOME_BOTTOM_SHEET_COMMUNICATION = "affiliate home page - bottom sheet communication"
            const val AFFILIATE_EDUKASI_PAGE = "affiliate edukasi page"
            const val AFFILIATE_EDUKASI_CATEGORY_LANDING_ARTICLE = "affiliate edukasi page - category landing page - article"
            const val AFFILIATE_EDUKASI_CATEGORY_LANDING_EVENT = "affiliate edukasi page - category landing page - event"
            const val AFFILIATE_EDUKASI_CATEGORY_LANDING_TUTORIAL = "affiliate edukasi page - category landing page - tutorial"
            const val AFFILIATE_PROMOSIKAN_SSA_PAGE = "affiliate promosikan page - ssa shop list"
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
            const val CLICK_SEARCH_BOX = "click - search box"
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
            const val CLICK_TAMBAH_SOCIAL_MEDIA = "click - tambah social media"
            const val CLICK_BACK = "click - back"
            const val CLICK_SYARAT = "click - syarat dan ketentuan"
            const val CLICK_FILTER_DATE = "click - filter date"
            const val CLICK_SIMPAN = "click - simpan"
            const val CLICK_IMPRESSION_PRODUCT_PRODUK = "impression - product - produk yang dipromosikan"
            const val CLICK_PRODUCT_PRODUK_YANG = "click - product - produk yang dipromosikan"
            const val CLICK_SALIN_LINK_PRODUK_YANG_DIPROMOSIKAN = "click - salin link - produk yang dipromosikan"
            const val CLICK_GENERATED_LINK_HISTORY = "click - generated link history"
            const val CLICK_PERNAH_DIBELI_TAB = "click - pernah dibeli tab"
            const val CLICK_PERNAH_DILIHAT_TAB = "click - pernah dilihat tab"
            const val IMPRESSION_TRANSACTION_CARD = "impression - transaction card"
            const val IMPRESSION_PRODUK_YANG_DIPROMOSIKAN = "impression - product - produk yang dipromosikan"
            const val CLICK_PRODUCT_PRODUL_YANG_DIPROMOSIKAN = "click - product - produk yang dipromosikan"
            const val IMPRESSION_DAFTAR_LINK_PRODUK = "impression - product - daftar link produk"
            const val CLICK_SALIN_LINK_DAFTAR_LINK_PRODUK = "click - salin link - daftar link produk"
            const val CLICK_PRODUCT_DAFTAR_LINK_PRODUK = "click - product - daftar link produk"
            const val IMPRESSION_PRODUCT_PERNAH_DIBELI = "impression - product - pernah dibeli"
            const val IMPRESSION_PRODUCT_PERNAH_DILIHAT = "impression - product - pernah dilihat"
            const val PROMISIKAN_PERNAH_DIBELI = "click - promosikan - pernah dibeli"
            const val PROMOSIKAN_PERNAH_DILIHAT = "click - promosikan - pernah dilihat"
            const val IMPRESSION_PRODUCT_SEARCH_RESULT_PAGE = "impression - product - search result page"
            const val IMPRESSION_SHOP_SEARCH_RESULT_PAGE = "impression - shop - search result page"
            const val CLICK_PROMOSIKAN_SEARCH_RESULT_PAGE = "click - promosikan - search result page"
            const val CLICK_SALIN_LINK_RESULT_PAGE = "click - salin link - search result page"
            const val CLICK_TOTAL_KOMISI_CARD = "click - total komisi card"
            const val CLICK_KLIK_CARD = "click - klik card"
            const val IMPRESSION_TICKER_COMMUNICATION = "impression - ticker communication"
            const val CLICK_TICKER_COMMUNICATION = "click - ticker communication"
            const val CLICK_CLOSE = "click - close"
            const val CLICK_PRIMARY_BUTTON = "click - primary button"
            const val CLICK_SECONDARY_BUTTON = "click - secondary button"
            const val CLICK_SHOP_LINK_DENGAN_PERFORMA = "click - shop - link dengan performa"
            const val CLICK_SALIN_LINK_SHOP_LINK_DENGAN_PERFORMA = "click - salin link - shop -  link dengan performa"
            const val CLICK_SALIN_LINK_SHOP_SEARCH_RESULT = "click - salin link - shop - search result page"
            const val CLICK_SHOP_SEARCH_RESULT_PAGE = "click - shop - search result page"
            const val IMPRESSION_SHOP_LINK_DENGAN_PERFORMA = "impression - shop - link dengan performa"
            const val CLICK_PROMOTED_PAGE_FILTER_TAB = "click - promoted page filter tab"
            const val CLICK_MAIN_BANNER = "click - main banner"
            const val CLICK_EVENT_CARD = "click - event card"
            const val CLICK_LATEST_ARTICLE_CARD = "click - latest article card"
            const val CLICK_ARTICLE_CARD = "click - article card"
            const val IMPRESSION_ARTICLE_CARD = "impression - article card"
            const val IMPRESSION_TUTORIAL_CARD = "impression - tutorial card"
            const val CLICK_TUTORIAL_CARD = "click - tutorial card"
            const val IMPRESSION_MAIN_BANNER = "impression - main banner"
            const val IMPRESSION_EVENT_CARD = "impression - event card"
            const val IMPRESSION_LATEST_ARTICLE_CARD = "impression - latest article card"
            const val CLICK_ARTICLE_CATEGORY = "click - article category"
            const val IMPRESSION_ARTICLE_CATEGORY = "impression - article category"
            const val IMPRESSION_TUTORIAL_CATEGORY = "impression - tutorial category"
            const val IMPRESSION_EVENT_CATEGORY = "impression - event category"
            const val CLICK_TUTORIAL_CATEGORY = "click - tutorial category"
            const val CLICK_LIHAT_SEMUA_EVENT_CARD = "click - lihat semua - event card"
            const val CLICK_LIHAT_SEMUA_LATEST_ARTICLE_CARD = "click - lihat semua - latest article card"
            const val IMPRESSION_SOCIAL_MEDIA_CARD = "impression - social media card"
            const val CLICK_SOCIAL_MEDIA_CARD = "click - social media card"
            const val CLICK_KAMUS_AFFILIATE = "click - kamus affiliate"
            const val CLICK_BANTUAN = "click - bantuan"
            const val CLICK_EVENT_CATEGORY = "click - event category"
            const val CLICK_SSA_SHOP_BANNER = "click - ssa shop bannert"
            const val CLICK_SSA_SHOP_PAGE = "click - shop - ssa shop list"
        }
    }

    interface ScreenKeys {
        companion object {
            const val AFFILIATE_HOME_SCREEN_NAME = "/affiliate portal - home page"
            const val AFFILIATE_LOGIN_SCREEN_NAME = "/affiliate portal - affiliate registration page - "
            const val AFFILIATE_PORTFOLIO_NAME = "/affiliate portal - affiliate registration page - promotion channel input - "
            const val AFFILIATE_TERMS_AND_CONDITION = "/affiliate portal - affiliate registration page - terms and condition - "
            const val AFFILIATE_HOME_FRAGMENT = "/affiliate portal - affiliate home page - "
            const val AFFILIATE_PROMOSIKAN_PAGE = "/affiliate portal - affiliate promosikan page"
            const val AFFILIATE_PENDAPATAN_PAGE = "/affiliate portal - affiliate pendapatan page"
            const val AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_ORDER_SHOP = "/affiliate portal - affiliate pendapatan page - transaction detail - order - shop"
            const val AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_ORDER_PRODUCT = "/affiliate portal - affiliate pendapatan page - transaction detail - order - product"
            const val AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_TRAFFIC_SHOP = "/affiliate portal - affiliate pendapatan page - transaction detail - traffic - shop"
            const val AFFILIATE_PENDAPATAN_PAGE_TRANSACTION_TRAFFIC_PRODUCT = "/affiliate portal - affiliate pendapatan page - transaction detail - traffic - product"
        }
    }

    interface ItemKeys {
        companion object {
            const val AFFILIATE_PERNAH_DIBEL = "/affiliate - promosikan pernah dibeli"
            const val AFFILIATE_PERNAH_DILIHAT = "/affiliate - promosikan pernah dilihat"
            const val AFFILIATE_TRANSACTION_PAGE = "/affiliate pendapatan page - transaction history"
            const val AFFILAITE_HOME_SELECT_CONTENT = "/affiliate home page - produk yang dipromosikan"
            const val AFFILIATE_DAFTAR_LINK_PRODUK = "/affiliate home page - daftar link produk"
            const val AFFILIATE_PROMOSIKAN_PERNAH_DIBEL = "/affiliate promosikan page - pernah dibeli"
            const val AFFILIATE_PROMOSIKAN_PERNAH_DILIHAT = "/affiliate promosikan page - pernah dilihat"
            const val AFFILIATE_SEARCH_PROMOSIKAN_CLICK = "/affiliate promosikan page - search result page"
            const val AFFILIATE_HOME_TICKER_COMMUNICATION = "/affiliate home page - ticker communication"
            const val AFFILIATE_PROMOSIKAN_TICKER_COMMUNICATION = "/affiliate promosikan page - ticker communication"
            const val AFFILIATE_PENDAPATAN_TICKER_COMMUNICATION = "/affiliate pendapatan page - ticker communication"
            const val AFFILAITE_HOME_SHOP_SELECT_CONTENT = "/affiliate home page - link dengan performa - shop"
            const val AFFILIATE_SEARCH_SHOP_CLICK = "/affiliate promosikan page - search result page - shop"
            const val AFFILIATE_EDUCATION_PAGE = "/affiliate edukasi page"
            const val AFFILIATE_SSA_SHOP_CLICK = "/affiliate promosikan page - ssa shop list"
        }
    }

    interface LabelKeys {
        companion object {
            const val INCOMING = "incoming"
            const val OUTGOING = "outgoing"
            const val LOGIN = "login"
            const val NON_LOGIN = "non login"
            const val DEPOSIT_TRAFFIC = "deposit traffic"
            const val DEPOSIT_ORDER = "deposit order"
            const val WITHDRAWAL = "withdrawal"
            const val ACTIVE = "active"
            const val INACTIVE = "inactive"
            const val SUCCESS = "success"
            const val FAIL = "fail"
            const val NOT_URL = "not URL"
            const val PRDOUCT_URL_NOT_FOUND = "product URL not found"
            const val NON_WHITELISTED_CATEGORIES = "non whitelisted categories"
            const val NON_PM_OS_SHOP = "non PM or OS shop"
            const val AVAILABLE = "available"
            const val EMPTY_STOCK = "empty stock"
            const val ALMOST_OOS = "almost OOS"
            const val SHOP_INACTIVE = "shop inactive"
            const val PRODUCT_INACTIVE = "product inactive"
            const val SHOP_ACTIVE = "shop active"
            const val SHOP_CLOSED = "shop closed"
        }
    }
}
