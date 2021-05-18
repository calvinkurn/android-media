package com.tokopedia.mvcwidget

import androidx.annotation.IntDef
import com.tokopedia.mvcwidget.MvcSource.Companion.DEFAULT
import com.tokopedia.mvcwidget.MvcSource.Companion.PDP
import com.tokopedia.mvcwidget.MvcSource.Companion.SHOP
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
        const val CURRENT_SITE = "currentSite"
        const val TOKOPOINT_BUSINESSUNIT = "tokopoints"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    }

    object Event {
        const val CLICK_SHOP = "clickShopPage"
        const val CLICK_MV = "clickMerchantVoucher"
        const val VIEW_MV = "viewMerchantVoucherIris"
        const val VIEW_SHOP = "viewShopPageIris"
        const val CLICK_KUPON = "clickCoupon"
    }

    object Category {
        const val MERCHANT_VOUCHER = "merchant voucher"
        const val SHOP_PAGE_BUYER = "shop page - buyer"
        const val REWARDS_CATEGORY = "kupon toko"
    }

    object Action {
        const val CLICK_COUPON_ENTRY_POINT = "click coupon entry point"
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
    }

    object Label {
        const val PDP_VIEW = "pdp view"
        const val SHOP_PAGE = "shop page"
    }

    fun fillCommonItems(map: MutableMap<String, Any>, userId: String?) {
        map[Constants.BUSINESS_UNIT] = "physical goods"
        map[Constants.CURRENT_SITE] = Constants.TOKOPEDIA_MARKETPLACE
        userId?.let {
            map[Constants.USER_ID] = userId
        }
    }

    //1 Pdp
    //16 Shop
    fun userClickEntryPoints(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.CLICK_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_ACTION] = Action.CLICK_COUPON_ENTRY_POINT
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"

            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_ACTION] = Action.CLICK_COUPON
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"

            }
        }
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //3, 18
    fun clickFollowButton(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.CLICK_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLICK_FOLLOW_WIDGET

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //4,10,19, 25
    fun viewFollowButtonToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.VIEW_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.VIEW_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        if (isSuccess) {
            map[Constants.EVENT_ACTION] = Action.VIEW_TOASTER_FOLLOW_SUCCESS
        } else {
            map[Constants.EVENT_ACTION] = Action.VIEW_TOASTER_FOLLOW_ERROR
        }

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //5,9,20,24,44,45 - DO NOT SEND TRACKERS YET - IT IS MUTUALLY DECIDED WITH PO
    fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.VIEW_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.VIEW_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }
        when (widgetType) {
            FollowWidgetType.FIRST_FOLLOW -> {
                map[Constants.EVENT_ACTION] = Action.VIEW_SHOP_FOLLOWERS_COUPON
            }
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                map[Constants.EVENT_ACTION] = Action.VIEW_MEMBERSHIP_COUPON
            }
            else -> {
                map[Constants.EVENT_ACTION] = Action.VIEW_REGULAR_COUPON
            }
        }
        fillCommonItems(map, userId)
//        getTracker().sendGeneralEvent(map)
    }

    //2,6,17,21
    fun viewWidgetImpression(@FollowWidgetType widgetType: String, shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()
        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.VIEW_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.VIEW_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }
        when (widgetType) {
            FollowWidgetType.FIRST_FOLLOW -> {
                map[Constants.EVENT_ACTION] = Action.VIEW_FOLLOW_WIDGET
            }
            FollowWidgetType.MEMBERSHIP_OPEN -> {
                map[Constants.EVENT_ACTION] = Action.VIEW_MEMBERSHIP_WIDGET
            }
            FollowWidgetType.MEMBERSHIP_CLOSE -> {
                map[Constants.EVENT_ACTION] = Action.VIEW_MEMBERSHIP_WIDGET
            }
        }
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //7, 22
    fun clickJadiMemberButton(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.CLICK_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLICK_JADI_MEMBER
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //8,11,23,26
    fun viewJadiMemberToast(shopId: String, userId: String?, @MvcSource source: Int, isSuccess: Boolean) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.VIEW_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.VIEW_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        if (isSuccess) {
            map[Constants.EVENT_ACTION] = Action.VIEW_TOASTER_JADI_MEMBER_SUCCESS
        } else {
            map[Constants.EVENT_ACTION] = Action.VIEW_TOASTER_JADI_MEMBER_ERROR
        }

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //12, 27
    fun clickCekInfoButton(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.CLICK_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLICK_CEK_INFO
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //13,28
    fun viewTokomemberBottomSheet(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.VIEW_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.VIEW_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.VIEW_TM_INFO
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //14, 29
    fun clickDaftarJadiMember(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.CLICK_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }
        map[Constants.EVENT_ACTION] = Action.CLICK_DAFTAR

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //15, 30
    fun closeMainBottomSheet(shopId: String, userId: String?, @MvcSource source: Int) {
        val map = mutableMapOf<String, Any>()

        when (source) {
            MvcSource.PDP -> {
                map[Constants.EVENT] = Event.CLICK_MV
                map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
                map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
            }
            MvcSource.SHOP -> {
                map[Constants.EVENT] = Event.CLICK_SHOP
                map[Constants.EVENT_CATEGORY] = Category.SHOP_PAGE_BUYER
                map[Constants.EVENT_LABEL] = "${Label.SHOP_PAGE}-$shopId"
            }
        }

        map[Constants.EVENT_ACTION] = Action.CLOSE_BOTTOMSHEET

        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43

    //Reward GTM for Bottomsheet CTA
    fun userClickBottomSheetCTA(label: String) {
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_KUPON
        map[Constants.EVENT_CATEGORY] = Category.REWARDS_CATEGORY
        map[Constants.EVENT_ACTION] = Action.CLICK_CEK_TOKO
        map[Constants.EVENT_LABEL] = label
        map[Constants.CURRENT_SITE] = Constants.TOKOPEDIA_MARKETPLACE
        map[Constants.BUSINESS_UNIT] = Constants.TOKOPOINT_BUSINESSUNIT

        getTracker().sendGeneralEvent(map)
    }

}

@Retention(AnnotationRetention.SOURCE)
@IntDef(DEFAULT, SHOP, PDP)
annotation class MvcSource {

    companion object {
        const val DEFAULT = 0
        const val SHOP = 1
        const val PDP = 2
    }
}
