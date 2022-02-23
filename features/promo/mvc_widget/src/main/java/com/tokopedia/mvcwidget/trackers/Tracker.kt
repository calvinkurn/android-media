package com.tokopedia.mvcwidget.trackers

import androidx.annotation.IntDef
import com.tokopedia.mvcwidget.trackers.MvcSource.Companion.DEFAULT
import com.tokopedia.mvcwidget.trackers.MvcSource.Companion.PDP
import com.tokopedia.mvcwidget.trackers.MvcSource.Companion.REWARDS
import com.tokopedia.mvcwidget.trackers.MvcSource.Companion.SHOP
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

/*
* Confusing Trackers
* 2,3, 19, 20
* */

object Tracker {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val TOKOPOINT_BUSINESSUNIT = "tokopoints"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val TOKOMEMBER_BUSINESSUNIT = "tokomember"
        const val PHYSICALGOODS_BUSINESSUNIT = "physical goods"
        const val MERCHANT_COUPONLIST_SCREEN_NAME = "mvcwidget/multishopverticallist"
        const val CURRENT_SITE = "currentSite"
        const val ECOMMERCE = "ecommerce"
    }

    object Event {
        const val CLICK_SHOP = "clickShopPage"
        const val CLICK_MV = "clickMerchantVoucher"
        const val VIEW_MV = "viewMerchantVoucherIris"
        const val VIEW_SHOP = "viewShopPageIris"
        const val CLICK_KUPON = "clickCoupon"
        const val CLICK_PRODUCT_CARD = "click product card"
        const val CLICK_SHOP_NAME = "click shop name"
        const val CLICK_COUPON_TITLE = "click coupon title"
        const val EVENT_VIEW_PROMO = "promoView"
        const val CLICK_MVC_PRODUCT_ID = "productId"
    }

    object Category {
        const val MERCHANT_VOUCHER = "merchant voucher"
        const val SHOP_PAGE_BUYER = "shop page - buyer"
        const val REWARDS_CATEGORY = "kupon toko"
        const val MERCHANT_VOUCHER_CLOSE = "merchant voucher closed"
    }

    object Action {
        const val CLICK_COUPON_ENTRY_POINT = "click coupon entry point"
        const val CLICK_TOKOMEMBER_ENTRY_POINT = "click coupon tokomember"
        const val CLICK_COUPON = "click coupon"
        const val VIEW_FOLLOW_WIDGET = "view follow widget"
        const val VIEW_MEMBERSHIP_WIDGET = "view membership widget"
        const val CLICK_FOLLOW_WIDGET = "click follow widget"
        const val CLICK_JADI_MEMBER = "click jadi member widget"
        const val CLICK_CEK_INFO = "click cek info"
        const val VIEW_TM_INFO = "view tokomember info"
        const val CLICK_DAFTAR = "click daftar jadi member widget"
        const val CLOSE_BOTTOMSHEET = "Close buttomsheet"
        const val VIEW_TOASTER_FOLLOW_SUCCESS = "view toaster follow success"
        const val VIEW_TOASTER_FOLLOW_ERROR = "view toaster follow error"
        const val VIEW_TOASTER_JADI_MEMBER_SUCCESS = "view toaster jadi member success"
        const val VIEW_TOASTER_JADI_MEMBER_ERROR = "view toaster jadi member error"
        const val VIEW_SHOP_FOLLOWERS_COUPON = "view shop followers coupon"
        const val VIEW_MEMBERSHIP_COUPON = "view membership coupon"
        const val VIEW_REGULAR_COUPON = "view regular coupon"
        const val CLICK_CEK_TOKO = "click cek toko"
        const val CLICK_LIHAT_SELENGKAPNYA = "click lihat selengkapnya"
        const val CLICK_MULAI_BELANJA = "click mulai belanja"
        const val VIEW_TOKOMEMBER = "view coupon tokomember"
        const val VIEW_MVC_COUPON = "impression-mvc"
        const val SEE_ENTRY_POINT = "see entry point"
    }
        object Label {
            const val PDP_VIEW = "pdp view"
            const val SHOP_PAGE = "shop page"
            const val MVC_CLOSE_VIEW_SELEGKAPANYA = "mvc_closed_lihat_selengkapnya"
            const val MVC_CLOSE_VIEW_MULAIBELANJA = "mvc_closed_mulai_belanja"
            const val MVC_CLOSE_CEK_INFO = "mvc_closed_cek_info"
        }

        fun fillCommonItems(map: MutableMap<String, Any>, userId: String?, businessUnit: String) {
            map[Constants.BUSINESS_UNIT] = businessUnit
            map[Constants.CURRENT_SITE] = Constants.TOKOPEDIA_MARKETPLACE
            userId?.let {
                map[Constants.USER_ID] = userId
            }
        }
    }

@Retention(AnnotationRetention.SOURCE)
@IntDef(DEFAULT, SHOP, PDP, REWARDS)
annotation class MvcSource {

    companion object {
        const val DEFAULT = 0
        const val SHOP = 1
        const val PDP = 2
        const val REWARDS = 3
        const val DISCO = 4

    }
}
